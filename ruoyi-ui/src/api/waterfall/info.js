import request from '@/utils/request'

// 查询waterfall列表
export function listInfo(query) {
  return request({
    url: '/waterfall/info/list',
    method: 'get',
    params: query
  })
}

// 查询waterfall详细
export function getInfo(id) {
  return request({
    url: '/waterfall/info/' + id,
    method: 'get'
  })
}

// 新增waterfall
export function addInfo(data) {
  return request({
    url: '/waterfall/info',
    method: 'post',
    data: data
  })
}

// 修改waterfall
export function updateInfo(data) {
  return request({
    url: '/waterfall/info',
    method: 'put',
    data: data
  })
}

// 删除waterfall
export function delInfo(id) {
  return request({
    url: '/waterfall/info/' + id,
    method: 'delete'
  })
}

// 删除文件
export function delMedia(id) {
  return request({
    url: '/waterfall/info/delmediaManager',
    method: 'get',
    params: {id}
  })
}
