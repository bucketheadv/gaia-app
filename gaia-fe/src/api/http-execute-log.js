import axios from '@/libs/api.request'

export const getHttpExecuteLogTableData = (data) => {
  return axios.request({
    url: '/api/executeLog/list',
    method: 'post',
    data: data
  })
}
