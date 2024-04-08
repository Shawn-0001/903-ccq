import request from '@/utils/request'

// 查询谐波电压数据列表
export function listHarmonicVoltage(query) {
  return request({
    url: '/IoT/harmonicVoltage/list',
    method: 'get',
    params: query
  })
}

// 查询谐波电压数据详细
export function getHarmonicVoltage(id) {
  return request({
    url: '/IoT/harmonicVoltage/' + id,
    method: 'get'
  })
}

// 查询最新一条电压数据
export function getLatestHarmonicVoltage(deviceId) {
  return request({
    url: '/IoT/harmonicVoltage/latest/' + deviceId,
    method: 'get'
  })
}

// 新增谐波电压数据
export function addHarmonicVoltage(data) {
  return request({
    url: '/IoT/harmonicVoltage',
    method: 'post',
    data: data
  })
}

// 修改谐波电压数据
export function updateHarmonicVoltage(data) {
  return request({
    url: '/IoT/harmonicVoltage',
    method: 'put',
    data: data
  })
}

// 删除谐波电压数据
export function delHarmonicVoltage(id) {
  return request({
    url: '/IoT/harmonicVoltage/' + id,
    method: 'delete'
  })
}
