package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysMaterialWelding;

/**
 * 焊装物料库存管理Service接口
 *
 * @author ruoyi
 * @date 2025-08-12
 */
public interface ISysMaterialWeldingService
{
    /**
     * 查询焊装物料库存管理
     *
     * @param id 焊装物料库存管理主键
     * @return 焊装物料库存管理
     */
    public SysMaterialWelding selectSysMaterialWeldingById(Long id);

    /**
     * 查询焊装物料库存管理列表
     *
     * @param sysMaterialWelding 焊装物料库存管理
     * @return 焊装物料库存管理集合
     */
    public List<SysMaterialWelding> selectSysMaterialWeldingList(SysMaterialWelding sysMaterialWelding);

    /**
     * 新增焊装物料库存管理
     *
     * @param sysMaterialWelding 焊装物料库存管理
     * @return 结果
     */
    public int insertSysMaterialWelding(SysMaterialWelding sysMaterialWelding);

    /**
     * 修改焊装物料库存管理
     *
     * @param sysMaterialWelding 焊装物料库存管理
     * @return 结果
     */
    public int updateSysMaterialWelding(SysMaterialWelding sysMaterialWelding);

    /**
     * 批量删除焊装物料库存管理
     *
     * @param ids 需要删除的焊装物料库存管理主键集合
     * @return 结果
     */
    public int deleteSysMaterialWeldingByIds(Long[] ids);

    /**
     * 删除焊装物料库存管理信息
     *
     * @param id 焊装物料库存管理主键
     * @return 结果
     */
    public int deleteSysMaterialWeldingById(Long id);

    /**
     * 转序涂装扣减库存
     * @param list 转序物料列表
     * @return 结果
     */
    public int transferStock(List<SysMaterialWelding> list);

    /**
     * 通过Excel转序扣减库存（按批次优先扣减旧批次）
     * @param excelData Excel中的物料数据
     * @return 结果
     */
    public int transferByExcel(List<SysMaterialWelding> excelData);
}
