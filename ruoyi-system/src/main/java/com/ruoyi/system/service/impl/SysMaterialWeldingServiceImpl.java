package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysMaterialWeldingMapper;
import com.ruoyi.system.domain.SysMaterialWelding;
import com.ruoyi.system.service.ISysMaterialWeldingService;
import org.springframework.transaction.annotation.Transactional;

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
}
