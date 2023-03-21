import request from '@/utils/request'

// 查询产品功能列表
export function listProduct_service(query) {
  return request({
    url: '/manager/product_service/list',
    method: 'get',
    params: query
  })
}

// 查询产品功能详细
export function getProduct_service(id) {
  return request({
    url: '/manager/product_service/' + id,
    method: 'get'
  })
}

// 新增产品功能
export function addProduct_service(data) {
  return request({
    url: '/manager/product_service',
    method: 'post',
    data: data
  })
}

// 修改产品功能
export function updateProduct_service(data) {
  return request({
    url: '/manager/product_service',
    method: 'put',
    data: data
  })
}

// 删除产品功能
export function delProduct_service(id) {
  return request({
    url: '/manager/product_service/' + id,
    method: 'delete'
  })
}
