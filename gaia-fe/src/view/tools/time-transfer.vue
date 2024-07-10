<template>
  <div class="ivu-card ivu-card-bordered">
    <div class="ivu-card-head">
      <p>
        <span>
          时间转换器
        </span>
      </p>
    </div>
    <div class="ivu-card-body">
      <el-form :model="form" label-position="right" label-width="150px" ref="datetimeForm" :rules="rules">
        <el-form-item label="类型">
          <el-select v-model="transferType">
            <el-option v-for="t in transferTypes" :key="t.value" :value="t.value" :label="t.label" />
          </el-select>
        </el-form-item>
        <div v-if="transferType === 1">
          <el-form-item prop="datetime" label="时间" :required="true">
            <el-date-picker type="datetime" v-model="form.datetime" value-format="yyyy-MM-dd HH:mm:ss" />
          </el-form-item>
        </div>
        <div v-if="transferType === 2">
          <el-form-item prop="timestamp" label="时间戳" :required="true">
            <el-input v-model="form.timestamp" />
          </el-form-item>
        </div>
        <el-form-item prop="timezone" label="时区" :required="true">
          <el-select v-model="form.timezone">
            <el-option v-for="t in timezones" :key="t" :value="t" :label="t">
              {{t}}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit('datetimeForm')">提交</el-button>
        </el-form-item>
      </el-form>
      <el-divider />
      <div v-if="result">
        转换结果: {{result}}
      </div>
    </div>
  </div>
</template>
<script lang="js">
import { getTimeTransferToDatetime, getTimeTransferToEpochMilli } from '@/api/time-transfer'

export default {
  data () {
    return {
      form: {
        datetime: null,
        timezone: 'GMT+08:00'
      },
      transferType: 1,
      transferTypes: [
        { value: 1, label: '时间转时间戳' },
        { value: 2, label: '时间戳转时间' }
      ],
      result: null,
      rules: {
        datetime: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        timezone: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ]
      },
      timezones: [
        'GMT-03:00',
        'GMT-02:00',
        'GMT-01:00',
        'GMT+00:00',
        'GMT+01:00',
        'GMT+02:00',
        'GMT+03:00',
        'GMT+04:00',
        'GMT+05:00',
        'GMT+05:30',
        'GMT+06:00',
        'GMT+07:00',
        'GMT+08:00',
        'GMT+09:00',
        'GMT+10:00',
        'GMT+11:00',
        'GMT+12:00'
      ]
    }
  },
  methods: {
    handleSubmit (form) {
      this.$refs[form].validate(async (valid) => {
        if (!valid) {
          return
        }
        if (this.transferType === 1) {
          getTimeTransferToEpochMilli(this.form).then(res => {
            const body = res.data
            this.result = body.data
          }).catch(err => {
            this.$Message.error({
              content: err
            })
          })
        } else if (this.transferType === 2) {
          getTimeTransferToDatetime(this.form).then(res => {
            const body = res.data
            this.result = body.data
          }).catch(err => {
            this.$Message.error({
              content: err
            })
          })
        }
      })
    }
  }
}
</script>
