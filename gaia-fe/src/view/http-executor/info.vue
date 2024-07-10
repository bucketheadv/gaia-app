<template>
  <div>
    <div class="i-layout-page-header">
      <div class="ivu-page-header">
        <div class="ivu-page-header-detail">
          <div class="ivu-page-header-main">
            <div class="ivu-page-header-row">
              <div class="ivu-page-header-title">HTTP执行器详情页</div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="ivu-mt ivu-card-dis-hover">
      <Card>
        <div class="ivu-card-body">
          <div class="ivu-description-list">
            <div class="ivu-description-list-title">基本信息</div>
            <Row>
              <Col span="8">
                <div class="ivu-description-term">名称：</div>
                <div class="ivu-description-detail">{{form.name}}</div>
              </Col>
              <Col span="8">
                <div class="ivu-description-term">调用类型：</div>
                <div class="ivu-description-detail">{{showCallType(form.callType)}}</div>
              </Col>
              <Col span="8">
                <div class="ivu-description-term">服务名称：</div>
                <div class="ivu-description-detail">{{form.serviceName}}</div>
              </Col>
            </Row>
            <Row>
              <Col span="8">
                <div class="ivu-description-term">URL：</div>
                <div class="ivu-description-detail">{{form.url}}</div>
              </Col>
              <Col span="8">
                <div class="ivu-description-term">HTTP Method：</div>
                <div class="ivu-description-detail">{{form.httpMethod}}</div>
              </Col>
              <Col span="8">
                <div class="ivu-description-term">超时时间/秒：</div>
                <div class="ivu-description-detail">{{form.timeout}}</div>
              </Col>
            </Row>
            <Row>
              <Col span="8">
                <div class="ivu-description-term">上线状态：</div>
                <div class="ivu-description-detail">{{form.onlineStatus ? '上线' : '下线'}}</div>
              </Col>
              <Col span="8">
                <div class="ivu-description-term">创建时间：</div>
                <div class="ivu-description-detail">{{form.createTime}}</div>
              </Col>
              <Col span="8">
                <div class="ivu-description-term">更新类型：</div>
                <div class="ivu-description-detail">{{form.updateTime}}</div>
              </Col>
            </Row>
          </div>
          <Divider />
          <div class="ivu-description-list">
            <div class="ivu-description-list-title">HTTP请求头</div>
            <div v-if="!form.httpHeaders || form.httpHeaders.length === 0">
              无
            </div>
            <Row v-for="(header, idx) in form.httpHeaders" v-bind:key="'header_' + idx">
              <Col span="4">
                <div class="ivu-description-term">参数名：</div>
                <div class="ivu-description-detail">{{header.name}}</div>
              </Col>
              <Col span="4">
                <div class="ivu-description-term">中文名：</div>
                <div class="ivu-description-detail">{{header.nameCN}}</div>
              </Col>
              <Col span="4">
                <div class="ivu-description-term">必填：</div>
                <div class="ivu-description-detail">{{header.required}}</div>
              </Col>
              <Col span="4">
                <div class="ivu-description-term">类型：</div>
                <div class="ivu-description-detail">{{header.type}}</div>
              </Col>
              <Col span="8">
                <div class="ivu-description-term">默认值：</div>
                <div class="ivu-description-detail">{{header.defaultValue}}</div>
              </Col>
              <Col span="4">
                <div class="ivu-description-term">禁止修改：</div>
                <div class="ivu-description-detail">{{header.uneditable}}</div>
              </Col>
            </Row>
          </div>
          <Divider />
          <div class="ivu-description-list">
            <div class="ivu-description-list-title">HTTP请求参数</div>
            <div v-if="!form.httpParams || form.httpParams.length === 0">
              无
            </div>
            <Row v-for="(param, idx) in form.httpParams" v-bind:key="'param_' + idx">
              <Col span="4">
                <div class="ivu-description-term">参数名：</div>
                <div class="ivu-description-detail">{{param.name}}</div>
              </Col>
              <Col span="4">
                <div class="ivu-description-term">中文名：</div>
                <div class="ivu-description-detail">{{param.nameCN}}</div>
              </Col>
              <Col span="4">
                <div class="ivu-description-term">必填：</div>
                <div class="ivu-description-detail">{{param.required}}</div>
              </Col>
              <Col span="4">
                <div class="ivu-description-term">类型：</div>
                <div class="ivu-description-detail">{{param.type}}</div>
              </Col>
              <Col span="4">
                <div class="ivu-description-term">默认值：</div>
                <div class="ivu-description-detail">{{param.defaultValue}}</div>
              </Col>
              <Col span="4">
                <div class="ivu-description-term">禁止修改：</div>
                <div class="ivu-description-detail">{{param.uneditable}}</div>
              </Col>
            </Row>
          </div>
          <Divider />
          <div class="ivu-description-list">
            <div class="ivu-description-list-title">执行日志</div>
            <execute-log :executor-id="form.id" />
          </div>
          <Divider/>
          <Button type="success" @click="goToExecute()">去执行</Button>&nbsp;
          <Button type="info" @click="goToEdit()">编辑</Button>
        </div>
      </Card>
    </div>
  </div>
</template>
<script lang="js">
import { getHttpExecutorInfo } from '@/api/http-executor'
import ExecuteLog from '@/view/http-executor/execute-log.vue'

export default {
  components: { ExecuteLog },
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
      callTypeMap: {
        HTTP_CALL: '普通HTTP请求',
        MICRO_SERVICE: '微服务'
      }
    }
  },
  methods: {
    fetchData (id) {
      getHttpExecutorInfo(id).then(res => {
        this.form = res.data.data
        if (this.form == null) {
          this.$Message.error('执行器不存在!')
        }
      }).catch(err => this.$Message.error('' + err))
    },
    goToExecute () {
      this.$router.push('/httpExecutor/execute?id=' + this.form.id)
    },
    goToEdit () {
      this.$router.push('/httpExecutor/form?id=' + this.form.id)
    },
    showCallType (callType) {
      return this.callTypeMap[callType]
    }
  }
}
</script>

<style lang="less">
.i-layout-page-header {
  margin: -12px -24px 0;
}

.ivu-page-header {
  padding: 16px 32px 0 32px;
  background: #fff;
  border-bottom: 1px solid #e8eaec;
}

.ivu-page-header-detail {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
}

.ivu-page-header-main {
  -webkit-box-flex: 0;
  -ms-flex: 0 1 auto;
  flex: 0 1 auto;
}

.ivu-page-header-main {
  -webkit-box-flex: 0;
  -ms-flex: 0 1 auto;
  flex: 0 1 auto;
}

.ivu-page-header-detail, .ivu-page-header-row {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
}

.ivu-page-header-title {
  display: inline-block;
  color: #17233d;
  font-weight: 500;
  font-size: 20px;
  margin-bottom: 10px;
}

.ivu-mt, .ivu-mt-16 {
  margin-top: 16px!important;
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

.ivu-description-detail {
  width: 100%;
  color: #515a6e;
}
</style>
