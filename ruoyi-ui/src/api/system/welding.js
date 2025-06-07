import request from '@/utils/request'

// 查询总成件库存管理列表
export function listWelding(query) {
  return request({
    url: '/system/welding/list',
    method: 'get',
    params: query
  })
}

// 查询总成件库存管理详细
export function getWelding(id) {
  return request({
    url: '/system/welding/' + id,
    method: 'get'
  })
}

// 新增总成件库存管理
export function addWelding(data) {
  return request({
    url: '/system/welding',
    method: 'post',
    data: data
  })
}

// 修改总成件库存管理
export function updateWelding(data) {
  return request({
    url: '/system/welding',
    method: 'put',
    data: data
  })
}

// 删除总成件库存管理
export function delWelding(id) {
  return request({
    url: '/system/welding/' + id,
    method: 'delete'
  })
}

// 导入冲压库存数据
export function importWelding(data) {
  return request({
    url: '/system/welding/importData',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 报工焊装库存数据
export function syncStamping(data) {
  return request({
    url: '/system/welding/syncStamping',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
