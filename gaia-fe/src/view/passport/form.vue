<template>
  <div class="ivu-card ivu-card-bordered">
    <div class="ivu-card-head">
      <p>
        <span>
          编辑密码本
        </span>
      </p>
    </div>
    <div class="ivu-card-body">
      <el-form :model="form" label-position="right" label-width="150px" ref="passportForm" :rules="ruleInline">
        <el-form-item label="ID" v-if="form.id">
          <Input v-model="form.id" :disabled="true" />
        </el-form-item>
        <el-form-item label="标题" prop="title" :required="true">
          <Input v-model="form.title"></Input>
        </el-form-item>
        <el-form-item label="URL" prop="url">
          <Input v-model="form.url"></Input>
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <Input v-model="form.username"></Input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <Input type="password" v-model="form.password"></Input>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <Input type="textarea" v-model="form.remark"></Input>
        </el-form-item>
        <el-form-item>
          <Button type="primary" @click="handleSubmit('passportForm')">提交</Button>&nbsp;
          <Button type="error" @click="handleReset('passportForm')">重置</Button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script lang="js">
import { createPassportInfo, getPassportInfo, updatePassportInfo } from '@/api/passport'

export default {
  mounted () {
    this.init(this.$route.query)
  },
  data () {
    return {
      form: {},
      ruleInline: {
        title: [
          { required: true, message: '标题不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    init (query) {
      if (query && query.id) {
        getPassportInfo(query.id).then(res => {
          const body = res.data
          if (!body.data) {
            this.$Message.error({
              content: '密码本信息不存在!'
            })
            return
          }
          this.form = body.data
        })
      } else if (query && query.copyFromId) {
        getPassportInfo(query.copyFromId).then(res => {
          const body = res.data
          if (!body.data) {
            this.$Message.error({
              content: '密码本信息不存在!'
            })
            return
          }
          body.data.id = null
          body.data.createTime = null
          body.data.updateTime = null
          this.form = body.data
        })
      }
    },
    handleSubmit (name) {
      this.$refs[name].validate(async (valid) => {
        if (valid) {
          const form = this.form
          let result = {}
          try {
            if (form.id) {
              result = await updatePassportInfo(form)
            } else {
              result = await createPassportInfo(form)
            }
            if (result.data.code === 0) {
              this.$Message.success('成功!')
              this.$router.push('/passport/list')
            } else {
              this.$Message.error('失败!')
            }
          } catch (e) {
            this.$Message.error('' + e)
          }
        } else {
          this.$Message.error('参数错误!')
        }
      })
    },
    handleReset (name) {
      this.$refs[name].resetFields()
    }
  }
}
</script>
