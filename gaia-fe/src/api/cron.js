import axios from '@/libs/api.request'
import * as qs from 'querystring'

export const getNextValidTimeAfterNow = (data) => {
  const query = qs.stringify(data)
  return axios.request({
    url: '/api/cron/getNextValidTimeAfterNow?' + query,
    method: 'get'
  })
}
