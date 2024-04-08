import request from '@/utils/request'

// 查询电压数据列表
export function listVoltage(query) {
  return request({
    url: '/IoT/voltage/list',
    method: 'get',
    params: query
  })
}

// 查询电压数据详细
export function getVoltage(id) {
  return request({
    url: '/IoT/voltage/' + id,
    method: 'get'
  })
}

// 精确查询电压数据列表
export function getVoltageByField(query) {
  return request({
    url: '/IoT/voltage/field',
    method: 'get',
    params: query
  })
}

// 查询最新一条电压数据
export function getLatestVoltage(deviceId) {
  return request({
    url: '/IoT/voltage/latest/' + deviceId,
    method: 'get'
  })
}

// 新增电压数据
export function addVoltage(data) {
  return request({
    url: '/IoT/voltage',
    method: 'post',
    data: data
  })
}

// 修改电压数据
export function updateVoltage(data) {
  return request({
    url: '/IoT/voltage',
    method: 'put',
    data: data
  })
}

// 删除电压数据
export function delVoltage(id) {
  return request({
    url: '/IoT/voltage/' + id,
    method: 'delete'
  })
}
