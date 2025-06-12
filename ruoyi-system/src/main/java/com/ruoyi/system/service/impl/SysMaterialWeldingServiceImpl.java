package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.Map;

import com.ruoyi.system.util.WebServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysMaterialWeldingMapper;
import com.ruoyi.system.domain.SysMaterialWelding;
import com.ruoyi.system.service.ISysMaterialWeldingService;

/**
 * 总成件库存管理Service业务层处理
 *
 * @author ruoyi
 * @date 2025-06-05
 */
@Service
public class SysMaterialWeldingServiceImpl implements ISysMaterialWeldingService
{
    @Autowired
    private SysMaterialWeldingMapper sysMaterialWeldingMapper;

    /**
     * 查询总成件库存管理
     *
     * @param id 总成件库存管理主键
     * @return 总成件库存管理
     */
    @Override
    public SysMaterialWelding selectSysMaterialWeldingById(Long id)
    {
        return sysMaterialWeldingMapper.selectSysMaterialWeldingById(id);
    }

    /**
     * 查询总成件库存管理列表
     *
     * @param sysMaterialWelding 总成件库存管理
     * @return 总成件库存管理
     */
    @Override
    public List<SysMaterialWelding> selectSysMaterialWeldingList(SysMaterialWelding sysMaterialWelding)
    {
        return sysMaterialWeldingMapper.selectSysMaterialWeldingList(sysMaterialWelding);
    }

    /**
     * 新增总成件库存管理
     *
     * @param sysMaterialWelding 总成件库存管理
     * @return 结果
     */
    @Override
    public int insertSysMaterialWelding(SysMaterialWelding sysMaterialWelding)
    {
        return sysMaterialWeldingMapper.insertSysMaterialWelding(sysMaterialWelding);
    }

    /**
     * 修改总成件库存管理
     *
     * @param sysMaterialWelding 总成件库存管理
     * @return 结果
     */
    @Override
    public int updateSysMaterialWelding(SysMaterialWelding sysMaterialWelding)
    {
        return sysMaterialWeldingMapper.updateSysMaterialWelding(sysMaterialWelding);
    }

    /**
     * 批量删除总成件库存管理
     *
     * @param ids 需要删除的总成件库存管理主键
     * @return 结果
     */
    @Override
    public int deleteSysMaterialWeldingByIds(Long[] ids)
    {
        return sysMaterialWeldingMapper.deleteSysMaterialWeldingByIds(ids);
    }

    /**
     * 删除总成件库存管理信息
     *
     * @param id 总成件库存管理主键
     * @return 结果
     */
    @Override
    public int deleteSysMaterialWeldingById(Long id)
    {
        return sysMaterialWeldingMapper.deleteSysMaterialWeldingById(id);
    }

    /**
     * 导入冲压库存管理数据
     *
     * @param stampingList 冲压库存管理列表
     * @param updateSupport 是否更新支持
     * @param operName 操作人姓名
     * @return 导入结果消息
     */
    @Override
    public String importWelding(List<SysMaterialWelding> stampingList, boolean updateSupport, String operName) {
        // 这里可以添加具体的导入逻辑，例如循环插入或更新数据
        for (SysMaterialWelding stamping : stampingList) {
            if (updateSupport) {
                // 如果支持更新，先尝试更新，更新失败则插入
                int updateResult = sysMaterialWeldingMapper.updateSysMaterialWelding(stamping);
                if (updateResult == 0) {
                    sysMaterialWeldingMapper.insertSysMaterialWelding(stamping);
                }
            } else {
                // 不支持更新，直接插入
                sysMaterialWeldingMapper.insertSysMaterialWelding(stamping);
            }
        }
        return "导入成功";
    }

    /**
     * 查询是否已经报工
     *
     * @param ids 报工物料数据id
     * @return 结果
     */
    // 查询已同步的记录ID，需要修改 传入的ids是一个列表
    public List<Long> selectSyncedIds(List<Long> ids) {
        return sysMaterialWeldingMapper.selectSyncedWeldingIds(ids);
    }

    /**
     * 报工物料数据
     *
     * @param materialList 报工物料数据列表
     * @return 结果
     */
    @Override
    public int syncStampingByMateriaId(List<SysMaterialWelding> materialList)
    {
        // 补充报工相关的逻辑处理：调用接口，计算数量等

        // 记录同步结果
        StringBuilder resultMsg = new StringBuilder();
        int successCount = 0;
        int failedCount = 0;

        for (SysMaterialWelding mat : materialList) {
            try {
                // 跳过已同步的记录
                if (mat.getSyncFlag() == 1){
                    continue;
                }
                // 调用接口，获取BOM明细
                // 返回类型需要等接口调通 看到返回值之后更新
                Map<String,String> stampingDetails = WebServiceUtils.getBOMData(mat.getMaterialId());
                if (stampingDetails.isEmpty()) {
                    resultMsg.append("焊装物料ID:").append(mat.getMaterialId()).append("未找到BOM明细\n");
                    failedCount++;
                    continue;
                }
                /**
                 * TO BE CONTINUE
                 * */
            } catch (Exception e) {
                resultMsg.append("焊装物料ID:").append(mat.getMaterialId()).append("同步失败:").append(e.getMessage()).append("\n");
                failedCount++;
                // 单个物料同步失败，继续处理其他物料
            }
        }
        return sysMaterialWeldingMapper.syncStampingByMateriaId(materialList);
    }
}
