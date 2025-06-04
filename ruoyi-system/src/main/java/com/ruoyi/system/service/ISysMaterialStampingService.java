package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysMaterialStamping;

/**
 * 冲压件库存管理Service接口
 *
 * @author ruoyi
 * @date 2025-06-04
 */
public interface ISysMaterialStampingService
{
    /**
     * 查询冲压件库存管理
     *
     * @param id 冲压件库存管理主键
     * @return 冲压件库存管理
     */
    public SysMaterialStamping selectSysMaterialStampingById(Long id);

    /**
     * 查询冲压件库存管理列表
     *
     * @param sysMaterialStamping 冲压件库存管理
     * @return 冲压件库存管理集合
     */
    public List<SysMaterialStamping> selectSysMaterialStampingList(SysMaterialStamping sysMaterialStamping);

    /**
     * 新增冲压件库存管理
     *
     * @param sysMaterialStamping 冲压件库存管理
     * @return 结果
     */
    public int insertSysMaterialStamping(SysMaterialStamping sysMaterialStamping);

    /**
     * 修改冲压件库存管理
     *
     * @param sysMaterialStamping 冲压件库存管理
     * @return 结果
     */
    public int updateSysMaterialStamping(SysMaterialStamping sysMaterialStamping);

    /**
     * 批量删除冲压件库存管理
     *
     * @param ids 需要删除的冲压件库存管理主键集合
     * @return 结果
     */
    public int deleteSysMaterialStampingByIds(Long[] ids);

    /**
     * 删除冲压件库存管理信息
     *
     * @param id 冲压件库存管理主键
     * @return 结果
     */
    public int deleteSysMaterialStampingById(Long id);

    /**
     * 导入冲压库存管理数据
     *
     * @param stampingList 冲压库存管理列表
     * @param updateSupport 是否更新支持
     * @param operName 操作人姓名
     * @return 导入结果消息
     */
    String importStamping(List<SysMaterialStamping> stampingList, boolean updateSupport, String operName);

}
