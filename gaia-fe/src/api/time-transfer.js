import qs from 'querystring'
import axios from '@/libs/api.request'

export const getTimeTransferToEpochMilli = (data) => {
  const query = qs.stringify(data)
  return axios.request({
    url: '/api/timeTransfer/toEpochMilli?' + query,
    method: 'get'
  })
}

export const getTimeTransferToDatetime = (data) => {
  const query = qs.stringify(data)
  return axios.request({
    url: '/api/timeTransfer/toDatetime?' + query,
    method: 'get'
  })
}
