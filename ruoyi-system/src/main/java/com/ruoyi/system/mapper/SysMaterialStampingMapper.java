package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysMaterialStamping;
import org.apache.ibatis.annotations.Param;

/**
 * 冲压件库存管理Mapper接口
 *
 * @author ruoyi
 * @date 2025-06-04
 */
public interface SysMaterialStampingMapper
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
     * 删除冲压件库存管理
     *
     * @param id 冲压件库存管理主键
     * @return 结果
     */
    public int deleteSysMaterialStampingById(Long id);

    /**
     * 批量删除冲压件库存管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysMaterialStampingByIds(Long[] ids);

    /**
     * 查询库存中是否存在该物料
     */
    public List<SysMaterialStamping> selectSysMaterialStampingByMatId(String stampingId);

    /**
     * 查询可用的库存批次（按入库时间升序排列）
     */
    public List<SysMaterialStamping> selectAvailableBatches(String stampingId);

    /**
     * 扣减批次库存（带乐观锁）
     */
    public int reduceBatchInventory(@Param("id") Long id,
                                    @Param("reduceNum") int reduceNum,
                                    @Param("version") int version);

    /**
     * 标记批次为已用完
     */
    int markBatchAsUsed(Long id);
}
