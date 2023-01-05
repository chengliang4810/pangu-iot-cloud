import request from '@/utils/request'

// 查询设备属性列表
export function listAttribute(query) {
  return request({
    url: '/manager/device_attribute/list',
    method: 'get',
    params: query
  })
}

// 查询设备属性详细
export function getAttribute(id) {
  return request({
    url: '/manager/device_attribute/' + id,
    method: 'get'
  })
}

// 新增设备属性
export function addAttribute(data) {
  return request({
    url: '/manager/device_attribute',
    method: 'post',
    data: data
  })
}

// 修改设备属性
export function updateAttribute(data) {
  return request({
    url: '/manager/device_attribute',
    method: 'put',
    data: data
  })
}

// 删除设备属性
export function delAttribute(id) {
  return request({
    url: '/manager/device_attribute/' + id,
    method: 'delete'
  })
}
