import axios from '@/libs/api.request'
import * as qs from 'querystring'

export const getNextTriggerTime = (data) => {
  const query = qs.stringify(data)
  return axios.request({
    url: '/api/cron/nextTriggerTime?' + query,
    method: 'get'
  })
}
