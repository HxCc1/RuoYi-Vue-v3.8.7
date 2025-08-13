package com.ruoyi.system.service.impl;

import java.util.Comparator;
import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysMaterialWeldingMapper;
import com.ruoyi.system.domain.SysMaterialWelding;
import com.ruoyi.system.service.ISysMaterialWeldingService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * 焊装物料库存管理Service业务层处理
 *
 * @author ruoyi
 * @date 2025-08-12
 */
@Service
public class SysMaterialWeldingServiceImpl implements ISysMaterialWeldingService
{
    @Autowired
    private SysMaterialWeldingMapper sysMaterialWeldingMapper;

    /**
     * 查询焊装物料库存管理
     *
     * @param id 焊装物料库存管理主键
     * @return 焊装物料库存管理
     */
    @Override
    public SysMaterialWelding selectSysMaterialWeldingById(Long id)
    {
        return sysMaterialWeldingMapper.selectSysMaterialWeldingById(id);
    }

    /**
     * 查询焊装物料库存管理列表
     *
     * @param sysMaterialWelding 焊装物料库存管理
     * @return 焊装物料库存管理
     */
    @Override
    public List<SysMaterialWelding> selectSysMaterialWeldingList(SysMaterialWelding sysMaterialWelding)
    {
        return sysMaterialWeldingMapper.selectSysMaterialWeldingList(sysMaterialWelding);
    }

    /**
     * 新增焊装物料库存管理
     *
     * @param sysMaterialWelding 焊装物料库存管理
     * @return 结果
     */
    @Override
    public int insertSysMaterialWelding(SysMaterialWelding sysMaterialWelding)
    {
        return sysMaterialWeldingMapper.insertSysMaterialWelding(sysMaterialWelding);
    }

    /**
     * 修改焊装物料库存管理
     *
     * @param sysMaterialWelding 焊装物料库存管理
     * @return 结果
     */
    @Override
    public int updateSysMaterialWelding(SysMaterialWelding sysMaterialWelding)
    {
        return sysMaterialWeldingMapper.updateSysMaterialWelding(sysMaterialWelding);
    }

    /**
     * 批量删除焊装物料库存管理
     *
     * @param ids 需要删除的焊装物料库存管理主键
     * @return 结果
     */
    @Override
    public int deleteSysMaterialWeldingByIds(Long[] ids)
    {
        return sysMaterialWeldingMapper.deleteSysMaterialWeldingByIds(ids);
    }

    /**
     * 删除焊装物料库存管理信息
     *
     * @param id 焊装物料库存管理主键
     * @return 结果
     */
    @Override
    public int deleteSysMaterialWeldingById(Long id)
    {
        return sysMaterialWeldingMapper.deleteSysMaterialWeldingById(id);
    }

    @Override
    @Transactional
    public int transferStock(List<SysMaterialWelding> list) {
        int count = 0;
        for (SysMaterialWelding material : list) {
            // 1. 校验参数
            if (material.getId() == null || material.getReduceNum() == null) {
                throw new ServiceException("转序数据不完整，缺少ID或扣减数量");
            }

            // 2. 查询当前库存
            SysMaterialWelding dbMaterial = sysMaterialWeldingMapper.selectSysMaterialWeldingById(material.getId());
            if (dbMaterial == null) {
                throw new ServiceException("物料不存在：" + material.getId());
            }

            // 3. 校验库存是否充足
            Long reduceNum = material.getReduceNum(); // 从前端传递的reduceNum获取扣减数量
            if (reduceNum == null || reduceNum < 1) {
                throw new ServiceException("扣减数量必须大于0：ID=" + material.getId());
            }
            if (dbMaterial.getNum() < reduceNum) {
                throw new ServiceException("物料库存不足：" + dbMaterial.getMaterialId() + " ,当前库存：" + dbMaterial.getNum()+",请求扣减：" + reduceNum);
            }

            // 4. 计算新库存并更新
            SysMaterialWelding updateEntity = new SysMaterialWelding();
            updateEntity.setId(material.getId());
            updateEntity.setNum(dbMaterial.getNum() - reduceNum);
            count += sysMaterialWeldingMapper.updateSysMaterialWelding(updateEntity);
        }
        return count;
    }

    @Override
    @Transactional
    public int transferByExcel(List<SysMaterialWelding> excelData) {
        int totalCount = 0;

        for (SysMaterialWelding excelItem : excelData) {
            String materialId = excelItem.getMaterialId();
            Long needReduce = excelItem.getReduceNum();
            Long remaining = needReduce; // 剩余需要扣减的数量

            // 1. 查询该物料的所有批次，按日期升序（旧批次优先）
            SysMaterialWelding query = new SysMaterialWelding();
            query.setMaterialId(materialId);
            List<SysMaterialWelding> batches = sysMaterialWeldingMapper.selectSysMaterialWeldingList(query);

            // 按转序日期升序排序（确保旧批次先扣减）
            batches.sort(Comparator.comparing(SysMaterialWelding::getProcedingDate));

            if (CollectionUtils.isEmpty(batches)) {
                throw new ServiceException("物料不存在：" + materialId);
            }

            // 2. 检查总库存是否充足
            Long totalStock = batches.stream().mapToLong(SysMaterialWelding::getNum).sum();
            if (totalStock < needReduce) {
                throw new ServiceException("物料总库存不足：" + materialId + "，当前总库存：" + totalStock + "，请求扣减：" + needReduce);
            }

            // 3. 按批次扣减
            for (SysMaterialWelding batch : batches) {
                if (remaining <= 0) {
                    break; // 已扣减完成
                }

                Long batchNum = batch.getNum();
                if (batchNum <= 0) {
                    continue; // 跳过空批次
                }

                Long reduceThisBatch = Math.min(remaining, batchNum);
                Long newNum = batchNum - reduceThisBatch;

                // 更新批次库存
                SysMaterialWelding updateEntity = new SysMaterialWelding();
                updateEntity.setId(batch.getId());
                updateEntity.setNum(newNum);
                sysMaterialWeldingMapper.updateSysMaterialWelding(updateEntity);

                remaining -= reduceThisBatch;
                totalCount++;
            }

            if (remaining > 0) {
                throw new ServiceException("扣减过程中出现异常，物料：" + materialId + " 仍有 " + remaining + " 未扣减");
            }
        }

        return totalCount;
    }

}
