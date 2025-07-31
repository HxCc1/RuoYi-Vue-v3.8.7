package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ruoyi.system.domain.SysMaterialStamping;
import com.ruoyi.system.mapper.SysMaterialStampingMapper;
import com.ruoyi.system.util.WebServiceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysMaterialWeldingMapper;
import com.ruoyi.system.domain.SysMaterialWelding;
import com.ruoyi.system.service.ISysMaterialWeldingService;
import com.ruoyi.common.exception.ServiceException;

/**
 * 总成件库存管理Service业务层处理
 *
 * @author ruoyi
 * @date 2025-06-05
 */
@Service
public class SysMaterialWeldingServiceImpl implements ISysMaterialWeldingService {
    private static final Logger log = LoggerFactory.getLogger(SysMaterialWeldingServiceImpl.class);
    @Autowired
    private SysMaterialWeldingMapper sysMaterialWeldingMapper;

    @Autowired
    private SysMaterialStampingMapper sysMaterialStampingMapper;

    /**
     * 查询总成件库存管理
     *
     * @param id 总成件库存管理主键
     * @return 总成件库存管理
     */
    @Override
    public SysMaterialWelding selectSysMaterialWeldingById(Long id) {
        return sysMaterialWeldingMapper.selectSysMaterialWeldingById(id);
    }

    /**
     * 查询总成件库存管理列表
     *
     * @param sysMaterialWelding 总成件库存管理
     * @return 总成件库存管理
     */
    @Override
    public List<SysMaterialWelding> selectSysMaterialWeldingList(SysMaterialWelding sysMaterialWelding) {
        return sysMaterialWeldingMapper.selectSysMaterialWeldingList(sysMaterialWelding);
    }

    /**
     * 新增总成件库存管理
     *
     * @param sysMaterialWelding 总成件库存管理
     * @return 结果
     */
    @Override
    public int insertSysMaterialWelding(SysMaterialWelding sysMaterialWelding) {
        return sysMaterialWeldingMapper.insertSysMaterialWelding(sysMaterialWelding);
    }

    /**
     * 修改总成件库存管理
     *
     * @param sysMaterialWelding 总成件库存管理
     * @return 结果
     */
    @Override
    public int updateSysMaterialWelding(SysMaterialWelding sysMaterialWelding) {
        return sysMaterialWeldingMapper.updateSysMaterialWelding(sysMaterialWelding);
    }

    /**
     * 批量删除总成件库存管理
     *
     * @param ids 需要删除的总成件库存管理主键
     * @return 结果
     */
    @Override
    public int deleteSysMaterialWeldingByIds(Long[] ids) {
        return sysMaterialWeldingMapper.deleteSysMaterialWeldingByIds(ids);
    }

    /**
     * 删除总成件库存管理信息
     *
     * @param id 总成件库存管理主键
     * @return 结果
     */
    @Override
    public int deleteSysMaterialWeldingById(Long id) {
        return sysMaterialWeldingMapper.deleteSysMaterialWeldingById(id);
    }

    /**
     * 导入冲压库存管理数据
     *
     * @param stampingList  冲压库存管理列表
     * @param updateSupport 是否更新支持
     * @param operName      操作人姓名
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
    public String syncStampingByMateriaId(List<SysMaterialWelding> materialList) {
        // 补充报工相关的逻辑处理：调用接口，计算数量等

        // 记录同步结果
        StringBuilder resultMsg = new StringBuilder();
        int successCount = 0;
        int failedCount = 0;

        for (SysMaterialWelding welding : materialList) {
            try {
                // 跳过已同步的记录
                if (welding.getSyncFlag() == 1) {
                    continue;
                }
                // 调用接口，获取BOM明细
                // 返回类型需要等接口调通 看到返回值之后更新
                Map<String, Integer> stampingDetails = WebServiceUtils.getBOMData(welding.getMaterialId());
                log.info("焊装物料ID: {} 获取BOM明细: {}", welding.getMaterialId(), stampingDetails);

                if (stampingDetails.isEmpty()) {
                    resultMsg.append("焊装物料ID:").append(welding.getMaterialId()).append("未找到BOM明细\n");
                    failedCount++;
                    continue;
                }

                // 按批次扣减冲压库存
                for (Map.Entry<String, Integer> entry : stampingDetails.entrySet()) {
                    String stampingId = entry.getKey();
                    Integer stampingNum = entry.getValue();
                    int reduceAmount = stampingNum * welding.getNum();
                    log.info("焊装总成物料ID: {} 冲压件ID: {} 数量: {}", welding.getMaterialId(), stampingId, reduceAmount);

                    // 判断库存中是否存在该冲压件
                    List<SysMaterialStamping> stampingList = sysMaterialStampingMapper.selectSysMaterialStampingByMatId(stampingId);
                    if (stampingList == null || stampingList.isEmpty()) {
                        log.info("冲压件ID: {} 不存在，无法扣减库存", stampingId);
                    } else {
                        // 执行库存扣减
                        reduceStampingInventoryByBatch(stampingId, reduceAmount);
                        log.info("冲压件ID: {} 扣减数量: {}", stampingId, reduceAmount);
                    }
                }
                // 标记为已同步
                String materialId = welding.getMaterialId();
                int syncMsg = sysMaterialWeldingMapper.updateWeldingSyncFlag( materialId); // updateSysMaterialWelding
                if (syncMsg == 1){
                    log.info("焊装物料ID: {} 同步状态已更新", welding.getMaterialId());
                } else {
                    log.info("焊装物料ID: {} 同步状态更新失败", welding.getMaterialId());
                }

                successCount++;


            } catch (Exception e) {
                resultMsg.append("焊装物料ID:").append(welding.getMaterialId()).append("同步失败:").append(e.getMessage()).append("\n");
                failedCount++;
                // 单个物料同步失败，继续处理其他物料
            }
        }
        resultMsg.append("同步完成，成功:").append(successCount).append("条，失败:").append(failedCount).append("条\n");
        return resultMsg.toString();
    }

    /**
     * 按批次扣减冲压库存（FIFO策略）
     */
    private void reduceStampingInventoryByBatch(String stampingId, int totalReduce) {
        int remaining = totalReduce;

        // 获取可用批次（按入库时间升序排列）
        List<SysMaterialStamping> stampingBatches = sysMaterialStampingMapper.selectAvailableBatches(stampingId);

        if (stampingBatches.isEmpty()) {
            throw new ServiceException("冲压件库存不足：" + stampingId + " 需扣减" + totalReduce + "，当前无可用库存");
        }

        for (SysMaterialStamping batch : stampingBatches) {
            if (remaining <= 0) {
                break;
            }

            // 当前批次可扣减的最大数量
            int availableReduce = Math.min(batch.getNum(), remaining);

            // 扣减库存（带乐观锁）
            int updateCount = sysMaterialStampingMapper.reduceBatchInventory(
                    batch.getId(),
                    availableReduce,
                    batch.getVersion()
            );

            if (updateCount == 0) {
                // 乐观锁冲突，重试或抛出异常
                throw new ServiceException("库存更新冲突，请稍后重试（批次ID:" + batch.getId() + "）");
            }

            // 更新剩余需要扣减的数量
            remaining -= availableReduce;

            // 如果批次已用完，标记为已用完
            if (batch.getNum() - availableReduce <= 0) {
                sysMaterialStampingMapper.markBatchAsUsed(batch.getId());
            }
        }

        // 如果仍有剩余，说明库存不足
        if (remaining > 0) {
            throw new ServiceException("冲压件库存不足：" + stampingId + " 需扣减" + totalReduce + "，当前可用" + (totalReduce - remaining));
        }
    }

}
