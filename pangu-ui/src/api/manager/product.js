import request from '@/utils/request'

// 查询产品列表
export function listProduct(query) {
  return request({
    url: '/manager/product/list',
    method: 'get',
    params: query
  })
}

// 查询产品详细
export function getProduct(id) {
  return request({
    url: '/manager/product/' + id,
    method: 'get'
  })
}

// 新增产品
export function addProduct(data) {
  return request({
    url: '/manager/product',
    method: 'post',
    data: data
  })
}

// 修改产品
export function updateProduct(data) {
  return request({
    url: '/manager/product',
    method: 'put',
    data: data
  })
}

// 删除产品
export function delProduct(id) {
  return request({
    url: '/manager/product/' + id,
    method: 'delete'
  })
}
