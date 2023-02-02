import request from '@/utils/request'

// 查询设备上下线规则列表
export function listDevice_status_function(query) {
  return request({
    url: '/manager/device_status_function/list',
    method: 'get',
    params: query
  })
}

// 查询设备上下线规则详细
export function getDevice_status_function(id) {
  return request({
    url: '/manager/device_status_function/' + id,
    method: 'get'
  })
}

// 新增设备上下线规则
export function addDevice_status_function(data) {
  return request({
    url: '/manager/device_status_function',
    method: 'post',
    data: data
  })
}

// 修改设备上下线规则
export function updateDevice_status_function(data) {
  return request({
    url: '/manager/device_status_function',
    method: 'put',
    data: data
  })
}

// 删除设备上下线规则
export function delDevice_status_function(id) {
  return request({
    url: '/manager/device_status_function/' + id,
    method: 'delete'
  })
}
