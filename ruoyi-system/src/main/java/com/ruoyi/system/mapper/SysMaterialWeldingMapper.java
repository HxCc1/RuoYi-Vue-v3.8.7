package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;

import com.ruoyi.system.domain.SysMaterialWelding;

/**
 * 总成件库存管理Mapper接口
 *
 * @author ruoyi
 * @date 2025-06-05
 */
public interface SysMaterialWeldingMapper
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
     * 删除总成件库存管理
     *
     * @param id 总成件库存管理主键
     * @return 结果
     */
    public int deleteSysMaterialWeldingById(Long id);

    /**
     * 批量删除总成件库存管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysMaterialWeldingByIds(Long[] ids);

    /**
     * 报工冲压物料
     *
     * @param materialList 需要报工的物料编码与数量
     * @return 结果
     */
    public int syncStampingByMateriaId(List<SysMaterialWelding> materialList);
}
