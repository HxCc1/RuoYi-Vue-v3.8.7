package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;

import com.ruoyi.system.domain.SysMaterialWelding;
import com.ruoyi.system.domain.SysMaterialWeldingPainting;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 查询物料是否已同步
     *
     * @param ids 需要查询的物料编码
     * @return 结果
     */
    List<Long> selectSyncedWeldingIds(List<Long> ids);

    /**
     * 查询物料是否已同步
     *
     * @param materialId
     * @return 结果
     */
    public int updateWeldingSyncFlag( @Param("materialId") String materialId);

    /**
     * 批量插入总成物料号转序信息
     * @param list 总成物料号转序信息列表
     * @return 插入条数
     */
    int batchInsertSysMaterialWeldingPainting(@Param("list") List<SysMaterialWeldingPainting> list);
}
