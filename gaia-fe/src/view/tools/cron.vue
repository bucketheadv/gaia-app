<template>
  <div class="ivu-card ivu-card-bordered">
    <div class="ivu-card-head">
      <p>
        <span>
          Cron表达式验证
        </span>
      </p>
    </div>
    <div class="ivu-card-body">
      <el-form :model="form" label-position="right" label-width="150px" ref="cronForm" :rules="rules">
        <el-row>
          <el-col :span="5">
            <el-form-item label="秒" prop="second">
              <el-input v-model="form.second" @change="resetCronField" />
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item label="分" prop="minute">
              <el-input v-model="form.minute" @change="resetCronField" />
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item label="小时" prop="hour">
              <el-input v-model="form.hour" @change="resetCronField" />
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item label="星期" prop="dayOfWeek">
              <el-input v-model="form.dayOfWeek" @change="resetCronField" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="5">
            <el-form-item label="日期" prop="dayOfMonth">
              <el-input v-model="form.dayOfMonth" @change="resetCronField" />
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item label="月份" prop="month">
              <el-input v-model="form.month" @change="resetCronField" />
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item label="年份" prop="year">
              <el-input v-model="form.year" @change="resetCronField" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="cron表达式" prop="cron" :required="true">
          <el-input v-model="form.cron" placeholder="请输入cron表达式，如 0 3 16 3 10 ? 2023" @change="resetFormField" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit('cronForm')">提交</el-button>
        </el-form-item>
        <el-divider />
        <div v-if="result && result.length > 0">
          <div style="font-weight: bold;">最近十次执行时间: </div>
          <div v-for="r in result" :key="r">
            {{r}}
          </div>
        </div>
      </el-form>
    </div>
  </div>
</template>
<script lang="js">
import { getNextValidTimeAfterNow } from '@/api/cron'

export default {
  mounted () {
    this.resetCronField()
  },
  data () {
    return {
      form: this.generateForm(),
      cron: '',
      rules: {
        cron: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ]
      },
      result: []
    }
  },
  methods: {
    handleSubmit (form) {
      this.$refs[form].validate(async (valid) => {
        if (!valid) {
          return
        }
        getNextValidTimeAfterNow(this.form).then(res => {
          const result = res.data
          if (result.error) {
            this.$Message.error({
              content: result.error
            })
            return
          }
          this.result = result.data
        }).catch(err => {
          console.log(err)
          this.$Message.error({
            content: err
          })
        })
      })
    },
    generateForm () {
      return {
        second: '*',
        minute: '*',
        hour: '*',
        dayOfMonth: '*',
        month: '*',
        dayOfWeek: '?',
        year: '*',
        cron: ''
      }
    },
    resetCronField () {
      const form = this.form
      form.cron = [form.second, form.minute, form.hour, form.dayOfMonth, form.month, form.dayOfWeek, form.year].join(' ')
    },
    resetFormField () {
      const cron = this.form.cron
      const values = cron.split(' ')
      this.form = {
        second: values[0],
        minute: values[1],
        hour: values[2],
        dayOfMonth: values[3],
        month: values[4],
        dayOfWeek: values[5],
        year: values[6],
        cron: cron
      }
    }
  }
}
</script>
