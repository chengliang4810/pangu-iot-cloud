import request from '@/utils/request'

// 查询功能执行记录列表
export function listService_execute_record(query) {
  return request({
    url: '/manager/service_execute_record/list',
    method: 'get',
    params: query
  })
}

// 查询功能执行记录详细
export function getService_execute_record(id) {
  return request({
    url: '/manager/service_execute_record/' + id,
    method: 'get'
  })
}

// 新增功能执行记录
export function addService_execute_record(data) {
  return request({
    url: '/manager/service_execute_record',
    method: 'post',
    data: data
  })
}

// 修改功能执行记录
export function updateService_execute_record(data) {
  return request({
    url: '/manager/service_execute_record',
    method: 'put',
    data: data
  })
}

// 删除功能执行记录
export function delService_execute_record(id) {
  return request({
    url: '/manager/service_execute_record/' + id,
    method: 'delete'
  })
}
