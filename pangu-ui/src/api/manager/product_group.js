import request from '@/utils/request'

// 查询产品分组列表
export function listProduct_group(query) {
  return request({
    url: '/manager/product_group/list',
    method: 'get',
    params: query
  })
}

// 查询产品分组详细
export function getProduct_group(id) {
  return request({
    url: '/manager/product_group/' + id,
    method: 'get'
  })
}

// 新增产品分组
export function addProduct_group(data) {
  return request({
    url: '/manager/product_group',
    method: 'post',
    data: data
  })
}

// 修改产品分组
export function updateProduct_group(data) {
  return request({
    url: '/manager/product_group',
    method: 'put',
    data: data
  })
}

// 删除产品分组
export function delProduct_group(id) {
  return request({
    url: '/manager/product_group/' + id,
    method: 'delete'
  })
}
