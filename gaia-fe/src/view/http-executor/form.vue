<template>
  <div class="ivu-card ivu-card-bordered">
    <div class="ivu-card-head">
      <p>
        <span>
          编辑HTTP执行器
        </span>
      </p>
    </div>
    <div class="ivu-card-body">
      <el-form :model="form" label-position="right" label-width="150px" ref="apiExecutorForm" :rules="ruleInline">
        <el-form-item label="ID" v-if="form.id">
          <Input v-model="form.id" :disabled="true" />
        </el-form-item>
        <el-form-item label="名称" prop="name" :required="true">
          <Input v-model="form.name"></Input>
        </el-form-item>
        <el-form-item label="调用类型" prop="callType" :required="true">
          <Select v-model="form.callType" clearable @on-change="onChangeCallType">
            <Option value="HTTP_CALL">普通HTTP请求</Option>
            <Option value="MICRO_SERVICE">微服务</Option>
          </Select>
        </el-form-item>
        <el-form-item label="服务名" prop="serviceName" v-if="isMicroService(form.callType)" :required="isMicroService(form.callType)">
          <Input v-model="form.serviceName"></Input>
        </el-form-item>
        <el-form-item label="URL" prop="url">
          <Input v-model="form.url"></Input>
        </el-form-item>
        <el-form-item label="HTTP Method" required prop="httpMethod">
          <Select v-model="form.httpMethod" clearable>
            <Option value="GET">GET</Option>
            <Option value="POST">POST</Option>
            <Option value="PUT">PUT</Option>
            <Option value="PATCH">PATCH</Option>
            <Option value="DELETE">DELETE</Option>
          </Select>
        </el-form-item>
        <el-form-item label="超时时间/秒" required prop="timeout">
          <InputNumber v-model="form.timeout" />
        </el-form-item>
        <el-form-item label="是否上线">
          <Checkbox v-model="form.onlineStatus">是</Checkbox>
        </el-form-item>
        <el-form-item label="HTTP 请求头">
          <div v-for="(v, idx) in form.httpHeaders" style="margin-top: 10px" v-bind:key="form.httpHeaders.indexOf(v)">
            <http-param-template :value="v" :prop-name="'httpHeaders.' + idx + '.name'" />
            <Button type="error" :size="'small'" @click="removeThisHeader(v)">删除此项 X</Button>
          </div>
          <Button type="success" :size="'small'" @click="addHttpHeader()" style="margin-bottom: 20px">新增</Button>
        </el-form-item>
        <el-form-item label="HTTP Query参数">
          <div v-for="(v, idx) in form.httpParams" style="margin-top: 10px" v-bind:key="form.httpParams.indexOf(v)">
            <http-param-template :value="v" :prop-name="'httpParams.' + idx + '.name'" />
            <Button type="error" :size="'small'" @click="removeThisParam(v)">删除此项 X</Button>
          </div>
          <Button type="success" :size="'small'" @click="addHttpParam()" style="margin-bottom: 20px">新增</Button>
        </el-form-item>
        <el-form-item label="HTTP Body参数">
          <div v-for="(v, idx) in form.httpBody" style="margin-top: 10px" v-bind:key="form.httpBody.indexOf(v)">
            <http-param-template :value="v" :prop-name="'httpBody.' + idx + '.name'" />
            <Button type="error" :size="'small'" @click="removeThisBody(v)">删除此项 X</Button>
          </div>
          <Button type="success" :size="'small'" @click="addHttpBody()" style="margin-bottom: 20px">新增</Button>
        </el-form-item>
        <el-form-item>
          <Button type="primary" @click="handleSubmit('apiExecutorForm')">提交</Button>&nbsp;
          <Button type="error" @click="handleReset('apiExecutorForm')">重置</Button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script lang="js">
import { createHttpExecutorInfo, getHttpExecutorInfo, updateHttpExecutorInfo } from '@/api/http-executor'
import HttpParamTemplate from '_c/http-param/HttpParamTemplate.vue'

export default {
  components: { HttpParamTemplate },
  mounted () {
    this.init(this.$route.query)
  },
  data () {
    const form = this.buildDefaultParams()
    return {
      form: form,
      ruleInline: {
        name: [
          { required: true, message: '名称不能为空', trigger: 'blur' }
        ],
        callType: [
          { required: true, message: '调用类型不能为空', trigger: 'blur' }
        ],
        httpMethod: [
          { required: true, message: 'HttpMethod不能为空', trigger: 'blur' }
        ],
        timeout: [
          { required: true, message: '超时时间不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    init (query) {
      if (query && query.id) {
        getHttpExecutorInfo(query.id).then(res => {
          this.form = res.data.data
          this.form.httpParams = this.form.httpParams || []
          this.form.httpHeaders = this.form.httpHeaders || []
          this.form.httpBody = this.form.httpBody || []
          this.resetRuleInline()
        })
      }
    },
    buildDefaultParams () {
      const query = this.$route.query
      let form = {}
      if (!query || !query.id) {
        form.timeout = 10
        form.httpHeaders = []
        form.httpParams = []
        form.httpBody = []
      }
      return form
    },
    handleSubmit (name) {
      this.$refs[name].validate(async (valid) => {
        if (valid) {
          const form = this.form
          let result = {}
          try {
            if (form.id) {
              result = await updateHttpExecutorInfo(form)
            } else {
              result = await createHttpExecutorInfo(form)
            }
            if (result.data.code === 0) {
              this.$Message.success('成功!')
              this.$router.push('/httpExecutor/list')
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
    onChangeCallType (value) {
      const ruleInline = Object.assign({}, this.ruleInline)
      if (this.isMicroService(value)) {
        ruleInline.serviceName = [
          { required: true, message: '服务名不能为空', trigger: 'blur' }
        ]
      } else {
        delete this.ruleInline.serviceName
      }
      this.ruleInline = ruleInline
    },
    isMicroService (callType) {
      return callType === 'MICRO_SERVICE'
    },
    addHttpHeader () {
      const form = this.form
      form.httpHeaders.push({})
      this.resetRuleInline()
    },
    addHttpParam () {
      const form = this.form
      form.httpParams.push({})
      this.resetRuleInline()
    },
    addHttpBody() {
      const form = this.form
      form.httpBody.push({})
      this.resetRuleInline()
    },
    removeThisHeader (v) {
      const form = this.form
      form.httpHeaders.splice(form.httpHeaders.indexOf(v), 1)
      this.resetRuleInline()
    },
    removeThisParam (v) {
      const form = this.form
      form.httpParams.splice(form.httpParams.indexOf(v), 1)
      this.resetRuleInline()
    },
    removeThisBody (v) {
      const form = this.form
      form.httpBody.splice(form.httpBody.indexOf(v), 1)
      this.resetRuleInline()
    },
    handleReset (name) {
      this.$refs[name].resetFields()
    },
    resetRuleInline () {
      const ruleInline = this.ruleInline
      for (let i = 0; i < this.form.httpHeaders.length; i++) {
        ruleInline['httpHeaders.' + i + '.name'] = [{ required: true, message: 'name不能为空', trigger: 'blur' }]
      }
      for (let i = 0; i <this.form.httpParams.length; i++) {
        ruleInline['httpParams.' + i + '.name'] = [{ required: true, message: 'name不能为空', trigger: 'blur' }]
      }
      this.ruleInline = ruleInline
    }
  }
}
</script>
