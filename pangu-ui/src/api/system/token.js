import request from '@/utils/request'

// 查询三方授权列表
export function listToken(query) {
  return request({
    url: '/system/token/list',
    method: 'get',
    params: query
  })
}

// 查询三方授权详细
export function getToken(id) {
  return request({
    url: '/system/token/' + id,
    method: 'get'
  })
}

// 新增三方授权
export function addToken(data) {
  return request({
    url: '/system/token',
    method: 'post',
    data: data
  })
}

// 修改三方授权
export function updateToken(data) {
  return request({
    url: '/system/token',
    method: 'put',
    data: data
  })
}

// 删除三方授权
export function delToken(id) {
  return request({
    url: '/system/token/' + id,
    method: 'delete'
  })
}
