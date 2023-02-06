import request from '@/utils/request'

// 查询产品功能关联关系列表
export function listProduct_service_relation(query) {
  return request({
    url: '/manager/product_service_relation/list',
    method: 'get',
    params: query
  })
}

// 查询产品功能关联关系详细
export function getProduct_service_relation(id) {
  return request({
    url: '/manager/product_service_relation/' + id,
    method: 'get'
  })
}

// 新增产品功能关联关系
export function addProduct_service_relation(data) {
  return request({
    url: '/manager/product_service_relation',
    method: 'post',
    data: data
  })
}

// 修改产品功能关联关系
export function updateProduct_service_relation(data) {
  return request({
    url: '/manager/product_service_relation',
    method: 'put',
    data: data
  })
}

// 删除产品功能关联关系
export function delProduct_service_relation(id) {
  return request({
    url: '/manager/product_service_relation/' + id,
    method: 'delete'
  })
}
