import request from '@/utils/request'

// 查询设备列表
export function listDevice(query) {
  return request({
    url: '/manager/device/list',
    method: 'get',
    params: query
  })
}

// 查询设备详细
export function getDevice(id) {
  return request({
    url: '/manager/device/' + id,
    method: 'get'
  })
}

// 新增设备
export function addDevice(data) {
  return request({
    url: '/manager/device',
    method: 'post',
    data: data
  })
}

// 修改设备
export function updateDevice(data) {
  return request({
    url: '/manager/device',
    method: 'put',
    data: data
  })
}

// 删除设备
export function delDevice(id) {
  return request({
    url: '/manager/device/' + id,
    method: 'delete'
  })
}
