import axios from '@/libs/api.request'

export const getHttpExecutorTableData = (data) => {
  return axios.request({
    url: '/api/executor/list',
    method: 'post',
    data: data
  })
}

export const getHttpExecutorInfo = (id) => {
  return axios.request({
    url: '/api/executor/info?id=' + id,
    method: 'get'
  })
}

export const createHttpExecutorInfo = (data) => {
  return axios.request({
    url: '/api/executor/create',
    method: 'post',
    data: data
  })
}

export const updateHttpExecutorInfo = (data) => {
  return axios.request({
    url: '/api/executor/update',
    method: 'post',
    data: data
  })
}

export const executeHttpExecutor = (data) => {
  return axios.request({
    url: '/api/executor/execute',
    method: 'post',
    data: data
  })
}

export const deleteHttpExecutor = (id) => {
  return axios.request({
    url: '/api/executor/delete?id=' + id,
    method: 'post'
  })
}
