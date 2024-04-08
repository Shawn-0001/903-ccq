import request from '@/utils/request'

// 查询谐波电流数据列表
export function listHarmonicCurrent(query) {
  return request({
    url: '/IoT/harmonicCurrent/list',
    method: 'get',
    params: query
  })
}

// 查询谐波电流数据详细
export function getHarmonicCurrent(id) {
  return request({
    url: '/IoT/harmonicCurrent/' + id,
    method: 'get'
  })
}

// 查询最新一条电流数据
export function getLatestHarmonicCurrent(deviceId) {
  return request({
    url: '/IoT/harmonicCurrent/latest/' + deviceId,
    method: 'get'
  })
}

// 新增谐波电流数据
export function addHarmonicCurrent(data) {
  return request({
    url: '/IoT/harmonicCurrent',
    method: 'post',
    data: data
  })
}

// 修改谐波电流数据
export function updateHarmonicCurrent(data) {
  return request({
    url: '/IoT/harmonicCurrent',
    method: 'put',
    data: data
  })
}

// 删除谐波电流数据
export function delHarmonicCurrent(id) {
  return request({
    url: '/IoT/harmonicCurrent/' + id,
    method: 'delete'
  })
}
