<template>
  <div class="ivu-card ivu-card-bordered">
    <div class="ivu-card-head">
      <p>
        <span>
          执行HTTP执行器（ #{{form.id}} ）:【 {{form.name}} 】
        </span>
      </p>
    </div>
    <div class="ivu-card-body">
      <el-form :model="data" ref="executorForm" label-position="right" label-width="150px" :rules="ruleInline">
        <div class="ivu-mt ivu-card-dis-hover">
          <Card>
            <div class="ivu-card-body">
              <div class="ivu-description-list">
                <div class="ivu-description-list-title">基本信息</div>
                <Row>
                  <Col span="8">
                    <div class="ivu-description-term">ID：</div>
                    <div class="ivu-description-detail">{{form.id}}</div>
                  </Col>
                  <Col span="8">
                    <div class="ivu-description-term">名称：</div>
                    <div class="ivu-description-detail">{{form.name}}</div>
                  </Col>
                  <Col span="8">
                    <div class="ivu-description-term">URL：</div>
                    <div class="ivu-description-detail">{{form.url}}</div>
                  </Col>
                </Row>
              </div>
            </div>
          </Card>
        </div>
        <div class="ivu-mt ivu-card-dis-hover">
          <Card>
            <div class="ivu-card-body">
              <div class="ivu-description-list">
                <div class="ivu-description-list-title">Http请求头</div>
                <div v-if="!form.httpHeaders || form.httpHeaders.length === 0">
                  无
                </div>
                <Row v-else v-for="(header, idx) in form.httpHeaders" v-bind:key="'header' + idx">
                  <el-form-item :prop="'headers.' + header.name" :label="header.nameCN + ' (' + header.name + ')'" :required="header.required">
                    <el-input type="text" v-if="header.type === 'string'" :disabled="header.uneditable" v-model="data.headers[header.name]" />
                    <InputNumber v-if="header.type === 'long'" :disabled="header.uneditable" v-model="data.headers[header.name]" style="width: 100%" />
                    <el-date-picker v-if="header.type ==='date' || header.type ==='datetime'" :type="header.type" :disabled="header.uneditable" v-model="data.headers[header.name]"/>
                    <el-checkbox v-if="header.type === 'boolean'" :disabled="header.uneditable" v-model="data.headers[header.name]" />
                  </el-form-item>
                </Row>
              </div>
            </div>
          </Card>
        </div>
        <div class="ivu-mt ivu-card-dis-hover">
          <Card>
            <div class="ivu-card-body">
              <div class="ivu-description-list">
                <div class="ivu-description-list-title">Http请求参数</div>
                <div v-if="!form.httpParams || form.httpParams.length === 0">
                  无
                </div>
                <Row v-else v-for="(param, idx) in form.httpParams" v-bind:key="'param' + idx">
                  <el-form-item :prop="'params.' + param.name" :label="param.nameCN + ' (' + param.name + ')'" :required="param.required">
                    <el-input type="text" v-if="param.type === 'string'" :disabled="param.uneditable" v-model="data.params[param.name]" />
                    <InputNumber v-if="param.type === 'long'" :disabled="param.uneditable" v-model="data.params[param.name]" style="width: 100%" />
                    <el-date-picker v-if="param.type ==='date' || param.type ==='datetime'" :type="param.type" :disabled="param.uneditable" v-model="data.headers[param.name]" />
                    <el-checkbox v-if="param.type === 'boolean'" :disabled="param.uneditable" v-model="data.params[param.name]">是</el-checkbox>
                    <el-select v-if="param.type === 'select'" v-model="data.params[param.name]">
                      <el-option v-for="v in (param.availableValues || '').split('\n')" :key="v" :value="v.split(',')[0]">
                        {{v.split(',')[1] || v.split(',')[0]}}
                      </el-option>
                    </el-select>
                  </el-form-item>
                </Row>
              </div>
            </div>
          </Card>
        </div>
        <div class="ivu-mt ivu-card-dis-hover">
          <Button type="primary" @click="handleExecute('executorForm')" :disabled="running">执行</Button>
        </div>
      </el-form>
    </div>
    <div class="ivu-card ivu-card-bordered">
      <div class="ivu-card-head">
        <p>
        <span>
          执行结果:
        </span>
        </p>
      </div>
<!--      <json-viewer :value="executeResponse || {}" :expand-depth="10" boxed sort :show-array-index="false" copyable theme="my-awesome-json" />-->
      <div v-if="!executeResponse || (typeof executeResponse === 'string')" style="padding: 10px 20px">
        {{executeResponse}}
      </div>
      <div v-else>
        <json-views :json="executeResponse || {}" :deep="10" />
      </div>
    </div>
  </div>
</template>

<script lang="js">
import { executeHttpExecutor, getHttpExecutorInfo } from '@/api/http-executor'
import jsonViews from '_c/json-view/JsonViews.vue'
// import jsonView from '_c/json-view'

export default {
  components: { jsonViews },
  mounted () {
    const query = this.$route.query
    if (query && query.id) {
      this.fetchData(query.id)
    } else {
      this.$Message.error('参数错误!')
    }
  },
  data () {
    return {
      form: {},
      data: {
        headers: {},
        params: {}
      },
      ruleInline: {},
      running: false,
      executeResponse: null
    }
  },
  methods: {
    fetchData (id) {
      getHttpExecutorInfo(id).then(res => {
        this.form = res.data.data
        if (this.form == null) {
          this.$Message.error('执行器不存在!')
          return
        }
        const headers = {}
        let ruleInline = {}
        for (let header of this.form.httpHeaders) {
          headers[header.name] = this.getDefaultValue(header)
          this.buildRuleInline(ruleInline, header, 'headers')
        }
        const params = {}
        for (let param of this.form.httpParams) {
          params[param.name] = this.getDefaultValue(param)
          this.buildRuleInline(ruleInline, param, 'params')
        }
        this.data = {
          headers: headers,
          params: params
        }
        this.ruleInline = ruleInline
      }).catch(err => this.$Message.error('' + err))
    },
    buildRuleInline (ruleInline, data, type) {
      if (data.required) {
        ruleInline[type + '.' + data.name] = this.ruleInline[type + '.' + data.name] || []
        ruleInline[type + '.' + data.name] = [
          { required: true, message: '不能为空', trigger: 'blur' }
        ]
        // ruleInline[type] = this.ruleInline[type] || {}
        // ruleInline[type][data.name] = [
        //   { required: true, message: '不能为空', trigger: 'blur' }
        // ]
      }
    },
    getDefaultValue (data) {
      if (!data.defaultValue) {
        return null
      }
      if (data.type === 'long') {
        return Number(data.defaultValue)
      } else if (data.type === 'boolean') {
        return Boolean(data.defaultValue)
      } else if (data.type === 'date') {
        return new Date(data.defaultValue)
      }
      return data.defaultValue
    },
    async handleExecute (name) {
      this.running = true
      const params = this.data
      this.$refs[name].validate(valid => {
        if (!valid) {
          this.$Message.error('参数错误!')
          this.running = false
          return
        }
        params.id = this.form.id
        executeHttpExecutor(params).then(res => {
          this.running = false
          this.executeResponse = res.data
          this.$Message.success('执行成功!')
        }).catch(err => {
          this.$Message.error('' + err)
          this.running = false
        })
      })
    }
  }
}
</script>

<style lang="less">
.ivu-mt {
  margin-top: 10px;
}

.ivu-description-list-title {
  margin-bottom: 16px;
  color: #17233d;
  font-weight: 500;
  font-size: 14px;
}

.ivu-description-detail, .ivu-description-term {
  display: table-cell;
  padding-bottom: 16px;
  line-height: 20px;
}
.ivu-description-term {
  color: #17233d;
  white-space: nowrap;
}

// values are default one from jv-light template
.my-awesome-json-theme {
  background: #fff;
  white-space: nowrap;
  color: #525252;
  font-size: 14px;
  font-family: Consolas, Menlo, Courier, monospace;

  .jv-ellipsis {
    color: #999;
    background-color: #eee;
    display: inline-block;
    line-height: 0.9;
    font-size: 0.9em;
    padding: 0px 4px 2px 4px;
    border-radius: 3px;
    vertical-align: 2px;
    cursor: pointer;
    user-select: none;
  }
  .jv-button { color: #49b3ff }
  .jv-key { color: #111111 }
  .jv-item {
    &.jv-array { color: #111111 }
    &.jv-boolean { color: #fc1e70 }
    &.jv-function { color: #067bca }
    &.jv-number { color: #fc1e70 }
    &.jv-number-float { color: #fc1e70 }
    &.jv-number-integer { color: #fc1e70 }
    &.jv-object { color: #111111 }
    &.jv-undefined { color: #e08331 }
    &.jv-string {
      color: #42b983;
      word-break: break-word;
      white-space: normal;
    }
  }
  .jv-code {
    .jv-toggle {
      &:before {
        padding: 0px 2px;
        border-radius: 2px;
      }
      &:hover {
        &:before {
          background: #eee;
        }
      }
    }
  }
}
</style>
