import request from '@/utils/request'

// 查询原始数据历史列表
export function listOriginalHistory(query) {
  return request({
    url: '/IoT/originalHistory/list',
    method: 'get',
    params: query
  })
}

// 查询原始数据历史详细
export function getOriginalHistory(id) {
  return request({
    url: '/IoT/originalHistory/' + id,
    method: 'get'
  })
}

// 新增原始数据历史
export function addOriginalHistory(data) {
  return request({
    url: '/IoT/originalHistory',
    method: 'post',
    data: data
  })
}

// 修改原始数据历史
export function updateOriginalHistory(data) {
  return request({
    url: '/IoT/originalHistory',
    method: 'put',
    data: data
  })
}

// 删除原始数据历史
export function delOriginalHistory(id) {
  return request({
    url: '/IoT/originalHistory/' + id,
    method: 'delete'
  })
}
