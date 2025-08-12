package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysMaterialWeldingMapper;
import com.ruoyi.system.domain.SysMaterialWelding;
import com.ruoyi.system.service.ISysMaterialWeldingService;

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
}
