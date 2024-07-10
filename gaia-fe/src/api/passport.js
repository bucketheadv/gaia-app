import axios from '@/libs/api.request'

export const getPassportTableData = (data) => {
  return axios.request({
    url: '/api/passportInfo/list',
    method: 'post',
    data: data
  })
}

export const getPassportInfo = (id) => {
  return axios.request({
    url: '/api/passportInfo/info?id=' + id,
    method: 'get'
  })
}

export const createPassportInfo = (data) => {
  return axios.request({
    url: '/api/passportInfo/create',
    method: 'post',
    data: data
  })
}

export const updatePassportInfo = (data) => {
  return axios.request({
    url: '/api/passportInfo/update',
    method: 'post',
    data: data
  })
}
export const deletePassportInfo = (id) => {
  return axios.request({
    url: '/api/passportInfo/delete?id=' + id,
    method: 'post'
  })
}
