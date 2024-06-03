import request from '@/utils/request'

// 查询password列表
export function listManager(query) {
  return request({
    url: '/waterfall/manager/list',
    method: 'get',
    params: query
  })
}

// 查询password详细
export function getManager(id) {
  return request({
    url: '/waterfall/manager/' + id,
    method: 'get'
  })
}

// 新增password
export function addManager(data) {
  return request({
    url: '/waterfall/manager',
    method: 'post',
    data: data
  })
}

// 修改password
export function updateManager(data) {
  return request({
    url: '/waterfall/manager',
    method: 'put',
    data: data
  })
}

// 删除password
export function delManager(id) {
  return request({
    url: '/waterfall/manager/' + id,
    method: 'delete'
  })
}
