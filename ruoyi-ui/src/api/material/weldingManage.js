import request from '@/utils/request'

// 查询焊装物料库存管理列表
export function listWeldingManage(query) {
  return request({
    url: '/system/weldingManage/list',
    method: 'get',
    params: query
  })
}

// 查询焊装物料库存管理详细
export function getWeldingManage(id) {
  return request({
    url: '/system/weldingManage/' + id,
    method: 'get'
  })
}

// 新增焊装物料库存管理
export function addWeldingManage(data) {
  return request({
    url: '/system/weldingManage',
    method: 'post',
    data: data
  })
}

// 修改焊装物料库存管理
export function updateWeldingManage(data) {
  return request({
    url: '/system/weldingManage',
    method: 'put',
    data: data
  })
}

// 删除焊装物料库存管理
export function delWeldingManage(id) {
  return request({
    url: '/system/weldingManage/' + id,
    method: 'delete'
  })
}

// 转序涂装扣减库存
export function transferWeldingManage(data) {
  return request({
    url: '/system/weldingManage/transfer',
    method: 'post',
    data: data
  })
}

// Excel转序扣减库存
export function transferByExcel(data) {
  return request({
    url: '/system/weldingManage/transferByExcel',
    method: 'post',
    data: data
  })
}
