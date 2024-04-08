import request from '@/utils/request'

// 查询设备列表列表
export function listDeviceList(query) {
  return request({
    url: '/IoT/deviceList/list',
    method: 'get',
    params: query
  })
}

// 精确查询设备列表列表
export function listDeviceByField(query) {
  return request({
    url: '/IoT/deviceList/field',
    method: 'get',
    params: query
  })
}

// 查询设备列表详细
export function getDeviceList(id) {
  return request({
    url: '/IoT/deviceList/' + id,
    method: 'get'
  })
}

// 新增设备列表
export function addDeviceList(data) {
  return request({
    url: '/IoT/deviceList',
    method: 'post',
    data: data
  })
}

// 修改设备列表
export function updateDeviceList(data) {
  return request({
    url: '/IoT/deviceList',
    method: 'put',
    data: data
  })
}

// 删除设备列表
export function delDeviceList(id) {
  return request({
    url: '/IoT/deviceList/' + id,
    method: 'delete'
  })
}
