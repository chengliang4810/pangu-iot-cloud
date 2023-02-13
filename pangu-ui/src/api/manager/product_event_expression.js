import request from '@/utils/request'

// 查询告警规则达式列表
export function listProduct_event_expression(query) {
  return request({
    url: '/manager/product_event_expression/list',
    method: 'get',
    params: query
  })
}

// 查询告警规则达式详细
export function getProduct_event_expression(id) {
  return request({
    url: '/manager/product_event_expression/' + id,
    method: 'get'
  })
}

// 新增告警规则达式
export function addProduct_event_expression(data) {
  return request({
    url: '/manager/product_event_expression',
    method: 'post',
    data: data
  })
}

// 修改告警规则达式
export function updateProduct_event_expression(data) {
  return request({
    url: '/manager/product_event_expression',
    method: 'put',
    data: data
  })
}

// 删除告警规则达式
export function delProduct_event_expression(id) {
  return request({
    url: '/manager/product_event_expression/' + id,
    method: 'delete'
  })
}
