<template>
  <div>
    <div class="i-layout-page-header">
      <div class="ivu-page-header">
        <div class="ivu-page-header-detail">
          <div class="ivu-page-header-main">
            <div class="ivu-page-header-row">
              <div class="ivu-page-header-title">密码本详情页</div>
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
                <div class="ivu-description-term">标题：</div>
                <div class="ivu-description-detail">{{form.title}}</div>
              </Col>
              <Col span="8">
                <div class="ivu-description-term">URL：</div>
                <div class="ivu-description-detail">
                  <a :href="form.url" v-if="form.url">{{form.url}}</a>
                </div>
              </Col>
              <Col span="8">
                <div class="ivu-description-term">备注：</div>
                <div class="ivu-description-detail">{{form.remark}}</div>
              </Col>
            </Row>
            <Row>
              <Col span="8">
                <div class="ivu-description-term">用户名：</div>
                <div class="ivu-description-detail">
                  {{form.username}}&nbsp;&nbsp;
                  <Button type="dashed" @click="copyText(form.username)">Copy</Button>
                </div>
              </Col>
              <Col span="8">
                <div class="ivu-description-term">密码：</div>
                <div class="ivu-description-detail">
                  {{showPassword(form.password)}}&nbsp;&nbsp;
                  <Button type="dashed" @click="copyText(form.password)">Copy</Button>
                </div>
              </Col>
            </Row>
          </div>
          <Divider />
          <Button type="success" @click="goToEdit()">编辑</Button>
        </div>
      </Card>
    </div>
  </div>
</template>
<script lang="js">
import { getPassportInfo } from '@/api/passport'

export default {
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
      form: {}
    }
  },
  methods: {
    fetchData (id) {
      getPassportInfo(id).then(res => {
        this.form = res.data.data
        if (this.form == null) {
          this.$Message.error('密码本不存在!')
        }
      }).catch(err => this.$Message.error('' + err))
    },
    goToEdit () {
      this.$router.push('/passport/form?id=' + this.form.id)
    },
    showPassword (text) {
      if (!text) {
        return text
      }
      const list = []
      for (let i = 0; i < text.length; i++) {
        list.push('*')
      }
      return list.join('')
    },
    copyText (text) {
      this.$copyText(text).then(() => this.$Message.success('拷贝成功'))
        .catch(e => this.$Message.error('拷贝失败: ' + e))
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
