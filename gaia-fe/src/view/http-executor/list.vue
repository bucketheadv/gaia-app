<template>
  <div>
    <el-form ref="form" :model="form" :rules="ruleInline" inline label-width="60" label-position="right">
      <el-form-item prop="name" label="名称">
        <el-input type="text" v-model="form.name" placeholder="名称" @keyup.enter.native="handleSubmit()" />
      </el-form-item>
      <el-form-item prop="callType" label="调用类型">
        <el-select v-model="form.callType" clearable style="width: 120px">
          <el-option value="1" label="普通HTTP请求">普通HTTP请求</el-option>
          <el-option value="2" label="微服务">微服务</el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <Button type="primary" @click="handleSubmit()">查询</Button>&nbsp;
        <Button type="success" @click="createApiExecutor()">新增</Button>
      </el-form-item>
    </el-form>

    <PageableTable border :columns="columns" :data="data" :page-info="pageInfo" :on-page-changed="pageChanged" :on-page-size-changed="pageSizeChanged" />
  </div>
</template>

<script lang="js">
import { deleteHttpExecutor, getHttpExecutorTableData } from '@/api/http-executor'
import * as qs from 'querystring'
import PageableTable from '_c/pageable-table/PageableTable.vue'

export default {
  components: { PageableTable },
  created () {
    this.fetchData(this.$route.query || {})
  },
  data () {
    const query = this.assignQueryParams()
    const self = this
    return {
      form: query.form,
      pageInfo: query.pageInfo,
      ruleInline: {
        name: [
          // { required: true, message: 'Please fill in the user name', trigger: 'blur' }
        ],
        callType: [
          // { required: true, message: 'Please fill in the password.', trigger: 'blur' },
          // { type: 'string', min: 6, message: 'The password length cannot be less than 6 bits', trigger: 'blur' }
        ]
      },
      callTypeMap: {
        HTTP_CALL: '普通HTTP请求',
        MICRO_SERVICE: '微服务'
      },
      columns: [
        {
          title: 'ID',
          key: 'id',
          width: 100,
          fixed: 'left'
        },
        {
          title: '执行器名称',
          key: 'name',
          width: 150
        },
        {
          title: '调用类型',
          key: 'callType',
          width: 120,
          render: (h, params) => {
            return h('div', this.callTypeMap[params.row.callType])
          }
        },
        {
          title: '服务名',
          key: 'serviceName',
          width: 100
        },
        {
          title: 'URL',
          key: 'url',
          width: 200,
          render: (h, params) => {
            const url = params.row.url
            if (!url) {
              return h('div', url)
            }
            return h('div', [
              h('a', {
                attrs: {
                  title: url,
                  // href: url,
                  target: '_blank'
                }
              }, this.showLongText(url, 100))
            ])
          }
        },
        {
          title: 'HTTP Method',
          key: 'httpMethod',
          width: 120
        },
        {
          title: '超时时间/秒',
          key: 'timeout',
          width: 120
        },
        {
          title: '上线状态',
          key: 'onlineStatus',
          width: 100,
          render: (h, params) => {
            const onlineStatus = params.row.onlineStatus
            let color = onlineStatus ? 'green' : 'red'
            let onlineInfo = onlineStatus ? '已上线' : '未上线'
            return h('div', { domProps: { style: 'color: ' + color } }, onlineInfo)
          }
        },
        {
          title: 'HTTP请求头',
          key: 'httpHeaders',
          width: 200,
          render: (h, params) => {
            const text = JSON.stringify(params.row.httpHeaders)
            // return h('div', { domProps: { title: text } }, this.showLongText(text, 100))
            return this.renderText(h, text, 100)
          }
        },
        {
          title: 'HTTP请求参数',
          key: 'httpParams',
          width: 200,
          render: (h, params) => {
            const text = JSON.stringify(params.row.httpParams)
            // return h('div', { domProps: { title: text } }, this.showLongText(text, 100))
            return this.renderText(h, text, 100)
          }
        },
        {
          title: '创建人',
          key: 'createdBy',
          width: 100
        },
        {
          title: '创建时间',
          key: 'createTime',
          width: 170
        },
        {
          title: '更新时间',
          key: 'updateTime',
          width: 170
        },
        {
          title: '操作',
          key: 'action',
          fixed: 'right',
          width: 200,
          render: (h, params) => {
            return h('div', [
              h('Button', {
                type: 'text',
                size: 'small',
                class: 'ivu-btn-dashed',
                style: {
                  marginTop: '5px',
                  marginRight: '5px'
                },
                on: {
                  click: () => {
                    self.$router.push('/httpExecutor/info?id=' + params.row.id)
                  }
                }
              }, '查看'),
              h('Button', {
                type: 'text',
                size: 'small',
                class: 'ivu-btn-warning',
                style: {
                  marginTop: '5px',
                  marginRight: '5px'
                },
                on: {
                  click: () => {
                    self.$router.push('/httpExecutor/form?id=' + params.row.id)
                  }
                }
              }, '编辑'),
              h('Button', {
                type: 'text',
                size: 'small',
                class: 'ivu-btn-success',
                style: {
                  marginRight: '5px',
                  marginTop: '5px',
                  marginBottom: '5px'
                },
                on: {
                  click: () => {
                    self.$router.push('/httpExecutor/execute?id=' + params.row.id)
                  }
                }
              }, '执行'),
              h('Button', {
                type: 'text',
                size: 'small',
                class: 'ivu-btn-error',
                style: {
                  marginTop: '5px',
                  marginBottom: '5px'
                },
                on: {
                  click: () => {
                    self.$confirm('您确认要删除吗?', '确认删除?')
                      .then(res => {
                        // 点击了确定
                        deleteHttpExecutor(params.row.id).then(res => {
                          self.$Message.success('删除成功!')
                          self.fetchData(self.$route.query)
                        }).catch((err) => {
                          self.$Message.error('删除失败!' + err)
                        })
                      }).catch(() => {
                        // 点击了取消
                        self.$Message.warning('您取消了删除')
                      })
                  }
                }
              }, '删除')
            ])
          }
        }
      ],
      data: []
    }
  },
  methods: {
    assignQueryParams () {
      const query = this.$route.query
      let obj = {}
      if (query) {
        obj.form = {
          name: query.name,
          callType: query.callType
        }
        obj.pageInfo = {
          page: parseInt(query.page || 1),
          pageSize: parseInt(query.pageSize) || 20
        }
        Object.assign(obj.form, obj.pageInfo)
      } else {
        obj.form = {
          name: '',
          callType: ''
        }
        obj.pageInfo = {
          page: 1,
          pageSize: 20
        }
      }
      return obj
    },
    fetchData (data) {
      getHttpExecutorTableData(data).then(res => {
        const body = res.data
        this.data = body.data.records
        this.pageInfo = {
          page: body.data.current,
          pageSize: body.data.size,
          total: body.data.total
        }
      })
    },
    handleSubmit () {
      this.$refs.form.validate((valid) => {
        if (valid) {
          const query = qs.stringify(this.form)
          if (query) {
            this.$router.push('?' + query)
            this.fetchData(this.form)
          } else {
            this.$router.push('')
          }
          this.$Message.success('成功!')
        } else {
          this.$Message.error('失败!')
        }
      })
    },
    createApiExecutor () {
      this.$router.push('/httpExecutor/form')
    },
    pageChanged (page) {
      const query = this.$route.query
      const params = Object.assign({}, query)
      params.page = page
      this.fetchData(params)
      this.$router.push('?' + qs.stringify(params))
    },
    pageSizeChanged (pageSize) {
      const query = this.$route.query
      const params = Object.assign({}, query)
      params.pageSize = pageSize
      this.fetchData(params)
      this.$router.push('?' + qs.stringify(params))
    },
    showLongText (text, length) {
      if (!text) {
        return ''
      }
      if (text.length > length) {
        return text.substring(0, length) + '...'
      }
      return text
    },
    showDetailBtn (text, length) {
      if (text && text.length > length) {
        return true
      }
      return false
    },
    showDetailInfo (text, length) {
      if (text && text.length > length) {
        return text.substring(0, length)
      }
      return text
    },
    renderText (h, text, length) {
      const showDetail = this.showDetailBtn(text, length)
      if (!showDetail) {
        return h('div', text)
      }
      return h('div', [
        h('div', {
          type: 'text',
          style: {
            float: 'left'
          }
        }, this.showDetailInfo(text, length) + '...'),
        h('Button', {
          type: 'text',
          size: 'small',
          class: 'ivu-btn-success',
          style: {
            marginTop: '5px',
            marginBottom: '5px',
            float: 'right'
          },
          on: {
            click: () => {
              this.$Modal.info({
                title: '详情',
                content: text,
                width: '80%'
              })
            }
          }
        }, '详情')
      ])
    }
  }
}
</script>

<style scoped lang="less">

</style>
