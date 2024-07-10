<template>
  <div>
    <PageableTable border :columns="columns" :data="data" :page-info="pageInfo" :on-page-changed="pageChanged" :on-page-size-changed="pageSizeChanged" />
  </div>
</template>
<script lang="js">
import PageableTable from '_c/pageable-table/PageableTable.vue'
import { getHttpExecuteLogTableData } from '@/api/http-execute-log'

export default {
  name: 'ExecuteLog',
  components: { PageableTable },
  props: {
    executorId: Number
  },
  mounted () {
    setTimeout(() => {
      if (this.executorId) {
        this.fetchData({ executorId: this.executorId })
      }
    }, 100)
  },
  data () {
    return {
      data: [],
      columns: [
        {
          title: 'id',
          key: 'id',
          width: 100
        },
        {
          title: '执行器ID',
          key: 'executorId',
          width: 100
        },
        {
          title: 'url',
          key: 'url',
          width: 200
        },
        {
          title: 'HTTP Method',
          key: 'httpMethod',
          width: 120
        },
        {
          title: 'HTTP请求头',
          key: 'httpHeaders',
          width: 200,
          render: (h, params) => {
            return this.renderText(h, params.row.httpHeaders, 100)
          }
        },
        {
          title: 'HTTP请求参数',
          key: 'httpParams',
          width: 200,
          render: (h, params) => {
            return this.renderText(h, params.row.httpParams, 100)
          }
        },
        {
          title: '耗时/毫秒',
          key: 'duration',
          width: 100
        },
        {
          title: '执行人',
          key: 'executeUser',
          width: 100
        },
        {
          title: '执行结果',
          key: 'executeResult',
          width: 100,
          render: (h, params) => {
            return h('div', {}, this.resultCodeMap[params.row.executeResult])
          }
        },
        {
          title: '执行返回值',
          key: 'executeResponse',
          width: 240,
          render: (h, params) => {
            return this.renderText(h, params.row.executeResponse, 100)
          }
        },
        {
          title: '执行时间',
          key: 'createTime',
          width: 170
        },
        {
          title: '更新时间',
          key: 'updateTime',
          width: 170
        }
      ],
      pageInfo: {},
      resultCodeMap: {
        0: '未知',
        1: '成功',
        2: '失败'
      }
    }
  },
  methods: {
    fetchData (params) {
      getHttpExecuteLogTableData(params).then(res => {
        const body = res.data
        this.data = body.data.records
        this.pageInfo = {
          page: body.data.current,
          pageSize: body.data.size,
          total: body.data.total
        }
      })
    },
    pageChanged (page) {
      this.fetchData({
        executorId: this.executorId,
        page: page,
        pageSize: this.pageInfo.pageSize
      })
    },
    pageSizeChanged (pageSize) {
      this.fetchData({
        executorId: this.executorId,
        page: this.pageInfo.page,
        pageSize: pageSize
      })
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
