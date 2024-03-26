import request from '@/utils/request'

// 查询功率数据列表
export function listPowerData(query) {
  return request({
    url: '/IoT/powerData/list',
    method: 'get',
    params: query
  })
}

// 查询功率数据详细
export function getPowerData(id) {
  return request({
    url: '/IoT/powerData/' + id,
    method: 'get'
  })
}

// 新增功率数据
export function addPowerData(data) {
  return request({
    url: '/IoT/powerData',
    method: 'post',
    data: data
  })
}

// 修改功率数据
export function updatePowerData(data) {
  return request({
    url: '/IoT/powerData',
    method: 'put',
    data: data
  })
}

// 删除功率数据
export function delPowerData(id) {
  return request({
    url: '/IoT/powerData/' + id,
    method: 'delete'
  })
}
