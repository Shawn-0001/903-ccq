import request from '@/utils/request'

// 查询电流数据列表
export function listCurrent(query) {
  return request({
    url: '/IoT/current/list',
    method: 'get',
    params: query
  })
}

// 查询电流数据详细
export function getCurrent(id) {
  return request({
    url: '/IoT/current/' + id,
    method: 'get'
  })
}

// 新增电流数据
export function addCurrent(data) {
  return request({
    url: '/IoT/current',
    method: 'post',
    data: data
  })
}

// 修改电流数据
export function updateCurrent(data) {
  return request({
    url: '/IoT/current',
    method: 'put',
    data: data
  })
}

// 删除电流数据
export function delCurrent(id) {
  return request({
    url: '/IoT/current/' + id,
    method: 'delete'
  })
}
