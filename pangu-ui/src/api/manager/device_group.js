import request from '@/utils/request'

// 查询设备分组列表
export function listDevice_group(query) {
  return request({
    url: '/manager/device_group/list',
    method: 'get',
    params: query
  })
}

// 查询设备分组详细
export function getDevice_group(id) {
  return request({
    url: '/manager/device_group/' + id,
    method: 'get'
  })
}

// 新增设备分组
export function addDevice_group(data) {
  return request({
    url: '/manager/device_group',
    method: 'post',
    data: data
  })
}

// 修改设备分组
export function updateDevice_group(data) {
  return request({
    url: '/manager/device_group',
    method: 'put',
    data: data
  })
}

// 删除设备分组
export function delDevice_group(id) {
  return request({
    url: '/manager/device_group/' + id,
    method: 'delete'
  })
}
