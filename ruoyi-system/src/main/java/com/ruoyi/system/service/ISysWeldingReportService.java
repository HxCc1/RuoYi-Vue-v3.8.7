package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysWeldingReport;

import java.util.List;

/**
 * 总成件库存管理Service接口
 *
 * @author ruoyi
 * @date 2025-06-05
 */
public interface ISysWeldingReportService
{
    /**
     * 查询总成件库存管理
     *
     * @param id 总成件库存管理主键
     * @return 总成件库存管理
     */
    public SysWeldingReport selectSysWeldingReportById(Long id);

    /**
     * 查询总成件库存管理列表
     *
     * @param sysWeldingReport 总成件库存管理
     * @return 总成件库存管理集合
     */
    public List<SysWeldingReport> selectSysWeldingReportList(SysWeldingReport sysWeldingReport);

    /**
     * 新增总成件库存管理
     *
     * @param sysWeldingReport 总成件库存管理
     * @return 结果
     */
    public int insertSysWeldingReport(SysWeldingReport sysWeldingReport);

    /**
     * 修改总成件库存管理
     *
     * @param sysWeldingReport 总成件库存管理
     * @return 结果
     */
    public int updateSysWeldingReport(SysWeldingReport sysWeldingReport);

    /**
     * 批量删除总成件库存管理
     *
     * @param ids 需要删除的总成件库存管理主键集合
     * @return 结果
     */
    public int deleteSysWeldingReportByIds(Long[] ids);

    /**
     * 删除总成件库存管理信息
     *
     * @param id 总成件库存管理主键
     * @return 结果
     */
    public int deleteSysWeldingReportById(Long id);

    /**
     * 导入冲压库存管理数据
     *
     * @param stampingList 冲压库存管理列表
     * @param updateSupport 是否更新支持
     * @param operName 操作人姓名
     * @return 导入结果消息
     */
    String importWelding(List<SysWeldingReport> stampingList, boolean updateSupport, String operName);

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
    public String syncStampingByMateriaId(List<SysWeldingReport> materialList);

}
