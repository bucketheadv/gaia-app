<template>
  <div>
    <Table width="100%" border :columns="columns" :data="data"></Table>
    <br/>
    <Page :total="pageInfo.total" :page-size="pageInfo.pageSize" :current="pageInfo.page" show-sizer="" show-elevator="" show-total="" @on-change="onPageChanged" @on-page-size-change="onPageSizeChanged"/>
  </div>
</template>

<script lang="js">
export default {
  name: 'PageableTable',
  props: {
    columns: {
      type: Array,
      default () {
        return []
      }
    },
    data: {
      type: Array,
      default () {
        return []
      }
    },
    pageInfo: {
      type: Object
    },
    onPageChanged: {
      type: Function
    },
    onPageSizeChanged: {
      type: Function
    }
  },
  data () {
    return {}
  }
}

</script>
