package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;

import com.ruoyi.system.domain.SysMaterialWelding;

/**
 * 总成件库存管理Service接口
 *
 * @author ruoyi
 * @date 2025-06-05
 */
public interface ISysMaterialWeldingService
{
    /**
     * 查询总成件库存管理
     *
     * @param id 总成件库存管理主键
     * @return 总成件库存管理
     */
    public SysMaterialWelding selectSysMaterialWeldingById(Long id);

    /**
     * 查询总成件库存管理列表
     *
     * @param sysMaterialWelding 总成件库存管理
     * @return 总成件库存管理集合
     */
    public List<SysMaterialWelding> selectSysMaterialWeldingList(SysMaterialWelding sysMaterialWelding);

    /**
     * 新增总成件库存管理
     *
     * @param sysMaterialWelding 总成件库存管理
     * @return 结果
     */
    public int insertSysMaterialWelding(SysMaterialWelding sysMaterialWelding);

    /**
     * 修改总成件库存管理
     *
     * @param sysMaterialWelding 总成件库存管理
     * @return 结果
     */
    public int updateSysMaterialWelding(SysMaterialWelding sysMaterialWelding);

    /**
     * 批量删除总成件库存管理
     *
     * @param ids 需要删除的总成件库存管理主键集合
     * @return 结果
     */
    public int deleteSysMaterialWeldingByIds(Long[] ids);

    /**
     * 删除总成件库存管理信息
     *
     * @param id 总成件库存管理主键
     * @return 结果
     */
    public int deleteSysMaterialWeldingById(Long id);

    /**
     * 导入冲压库存管理数据
     *
     * @param stampingList 冲压库存管理列表
     * @param updateSupport 是否更新支持
     * @param operName 操作人姓名
     * @return 导入结果消息
     */
    String importWelding(List<SysMaterialWelding> stampingList, boolean updateSupport, String operName);

    /**
     * 查询是否已经报工
     *
     * @param ids 报工物料数据id
     * @return 结果
     */
    public List<Long> selectSyncedIds(List<Long> ids);

    /**
     * 焊装报工冲压物料数据
     *
     * @param materialList 报工物料数据列表
     * @return 报工结果
     */
    public int syncStampingByMateriaId(List<SysMaterialWelding> materialList);

}
