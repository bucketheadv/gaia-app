<template>
  <div>
    <el-form ref="form" :model="form" inline label-width="60" label-position="right">
      <el-form-item prop="title" label="标题">
        <el-input type="text" v-model="form.title" placeholder="标题" @keyup.enter.native="handleSubmit()" />
      </el-form-item>
      <el-form-item prop="userId" label="用户id">
        <el-input v-model="form.userId" @keyup.enter.native="handleSubmit()" />
      </el-form-item>
      <el-form-item>
        <Button type="primary" @click="handleSubmit()">查询</Button>&nbsp;
        <Button type="success" @click="createPassportInfo()">新增</Button>
      </el-form-item>
    </el-form>

    <PageableTable border :columns="columns" :data="data" :page-info="pageInfo" :on-page-changed="pageChanged" :on-page-size-changed="pageSizeChanged" />
  </div>
</template>
<script lang="js">
import { deletePassportInfo, getPassportTableData } from '@/api/passport'
import PageableTable from '_c/pageable-table/PageableTable.vue'
import * as qs from 'querystring'

export default {
  components: { PageableTable },
  mounted () {
    this.fetchData(this.$route.query)
  },
  data () {
    const self = this
    return {
      form: {},
      data: [],
      columns: [
        {
          title: 'ID',
          key: 'id',
          width: 100
        },
        {
          title: '标题',
          key: 'title',
          width: 150
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
                  href: url,
                  target: '_blank'
                }
              }, this.showLongText(url, 100))
            ])
          }
        },
        {
          title: '用户名',
          key: 'username',
          width: 150,
          render: (h, params) => {
            const username = params.row.username
            if (!username) {
              return h('div', username)
            }
            return this.copyTextBtn(h, username, username)
          }
        },
        {
          title: '密码',
          key: 'password',
          width: 150,
          render: (h, params) => {
            const password = params.row.password
            if (!password) {
              return h('div', password)
            }
            return this.copyTextBtn(h, this.showPassword(password), password)
          }
        },
        {
          title: '备注',
          key: 'remark',
          width: 150
        },
        {
          title: '用户ID',
          key: 'userId',
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
          width: 180,
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
                    self.$router.push('/passport/info?id=' + params.row.id)
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
                    self.$router.push('/passport/form?id=' + params.row.id)
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
                    self.$router.push('/passport/form?copyFromId=' + params.row.id)
                  }
                }
              }, '复制'),
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
                        deletePassportInfo(params.row.id).then(res => {
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
      pageInfo: {}
    }
  },
  methods: {
    fetchData (query) {
      this.form = Object.assign({}, query)
      getPassportTableData(query).then(res => {
        const body = res.data
        this.data = body.data.records
        this.pageInfo = {
          page: body.data.current,
          pageSize: body.data.size,
          total: body.data.total
        }
      })
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
    copyTextBtn (h, showText, copyText) {
      return h('div', [
        h('div', {
          type: 'text',
          style: {
            float: 'left',
            paddingTop: '6px'
          }
        }, showText),
        h('Button', {
          type: 'text',
          size: 'small',
          class: 'ivu-btn-dashed',
          style: {
            float: 'right'
          },
          on: {
            click: () => {
              this.$copyText(copyText).then(() => this.$Message.success('拷贝成功'))
                .catch(e => this.$Message.error('拷贝失败: ' + e))
            }
          }
        }, 'Copy')
      ])
    },
    handleSubmit () {
      this.$router.push('?' + qs.stringify(this.form))
      this.fetchData(this.form)
    },
    createPassportInfo () {
      this.$router.push('/passport/form')
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
    }
  }
}
</script>
