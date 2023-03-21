import request from '@/utils/request'

// 查询告警记录列表
export function listProblem(query) {
  return request({
    url: '/manager/problem/list',
    method: 'get',
    params: query
  })
}

// 查询告警记录详细
export function getProblem(eventId) {
  return request({
    url: '/manager/problem/' + eventId,
    method: 'get'
  })
}

// 新增告警记录
export function addProblem(data) {
  return request({
    url: '/manager/problem',
    method: 'post',
    data: data
  })
}

// 修改告警记录
export function updateProblem(data) {
  return request({
    url: '/manager/problem',
    method: 'put',
    data: data
  })
}

// 删除告警记录
export function delProblem(eventId) {
  return request({
    url: '/manager/problem/' + eventId,
    method: 'delete'
  })
}
