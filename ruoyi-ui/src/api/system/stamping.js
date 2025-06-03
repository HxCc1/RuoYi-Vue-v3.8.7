import request from '@/utils/request'

// 查询冲压库存管理列表
export function listStamping(query) {
  return request({
    url: '/system/stamping/list',
    method: 'get',
    params: query
  })
}

// 查询冲压库存管理详细
export function getStamping(id) {
  return request({
    url: '/system/stamping/' + id,
    method: 'get'
  })
}

// 新增冲压库存管理
export function addStamping(data) {
  return request({
    url: '/system/stamping',
    method: 'post',
    data: data
  })
}

// 修改冲压库存管理
export function updateStamping(data) {
  return request({
    url: '/system/stamping',
    method: 'put',
    data: data
  })
}

// 删除冲压库存管理
export function delStamping(id) {
  return request({
    url: '/system/stamping/' + id,
    method: 'delete'
  })
}
