import request from '@/utils/request'

// 查询产品功能参数列表
export function listProduct_service_param(query) {
  return request({
    url: '/manager/product_service_param/list',
    method: 'get',
    params: query
  })
}

// 查询产品功能参数详细
export function getProduct_service_param(id) {
  return request({
    url: '/manager/product_service_param/' + id,
    method: 'get'
  })
}

// 新增产品功能参数
export function addProduct_service_param(data) {
  return request({
    url: '/manager/product_service_param',
    method: 'post',
    data: data
  })
}

// 修改产品功能参数
export function updateProduct_service_param(data) {
  return request({
    url: '/manager/product_service_param',
    method: 'put',
    data: data
  })
}

// 删除产品功能参数
export function delProduct_service_param(id) {
  return request({
    url: '/manager/product_service_param/' + id,
    method: 'delete'
  })
}
