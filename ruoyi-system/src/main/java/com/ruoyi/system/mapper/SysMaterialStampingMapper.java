package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysMaterialStamping;

/**
 * 冲压库存管理Mapper接口
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
public interface SysMaterialStampingMapper 
{
    /**
     * 查询冲压库存管理
     * 
     * @param id 冲压库存管理主键
     * @return 冲压库存管理
     */
    public SysMaterialStamping selectSysMaterialStampingById(Long id);

    /**
     * 查询冲压库存管理列表
     * 
     * @param sysMaterialStamping 冲压库存管理
     * @return 冲压库存管理集合
     */
    public List<SysMaterialStamping> selectSysMaterialStampingList(SysMaterialStamping sysMaterialStamping);

    /**
     * 新增冲压库存管理
     * 
     * @param sysMaterialStamping 冲压库存管理
     * @return 结果
     */
    public int insertSysMaterialStamping(SysMaterialStamping sysMaterialStamping);

    /**
     * 修改冲压库存管理
     * 
     * @param sysMaterialStamping 冲压库存管理
     * @return 结果
     */
    public int updateSysMaterialStamping(SysMaterialStamping sysMaterialStamping);

    /**
     * 删除冲压库存管理
     * 
     * @param id 冲压库存管理主键
     * @return 结果
     */
    public int deleteSysMaterialStampingById(Long id);

    /**
     * 批量删除冲压库存管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysMaterialStampingByIds(Long[] ids);
}
