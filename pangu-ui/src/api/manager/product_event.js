import request from '@/utils/request'

// 查询告警规则列表
export function listProduct_event(query) {
  return request({
    url: '/manager/product_event/list',
    method: 'get',
    params: query
  })
}

// 查询告警规则详细
export function getProduct_event(id) {
  return request({
    url: '/manager/product_event/' + id,
    method: 'get'
  })
}

// 新增告警规则
export function addProduct_event(data) {
  return request({
    url: '/manager/product_event',
    method: 'post',
    data: data
  })
}

// 修改告警规则
export function updateProduct_event(data) {
  return request({
    url: '/manager/product_event',
    method: 'put',
    data: data
  })
}

// 删除告警规则
export function delProduct_event(id) {
  return request({
    url: '/manager/product_event/' + id,
    method: 'delete'
  })
}
