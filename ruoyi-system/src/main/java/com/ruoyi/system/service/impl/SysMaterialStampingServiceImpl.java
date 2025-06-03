package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysMaterialStampingMapper;
import com.ruoyi.system.domain.SysMaterialStamping;
import com.ruoyi.system.service.ISysMaterialStampingService;

/**
 * 冲压库存管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
@Service
public class SysMaterialStampingServiceImpl implements ISysMaterialStampingService 
{
    @Autowired
    private SysMaterialStampingMapper sysMaterialStampingMapper;

    /**
     * 查询冲压库存管理
     * 
     * @param id 冲压库存管理主键
     * @return 冲压库存管理
     */
    @Override
    public SysMaterialStamping selectSysMaterialStampingById(Long id)
    {
        return sysMaterialStampingMapper.selectSysMaterialStampingById(id);
    }

    /**
     * 查询冲压库存管理列表
     * 
     * @param sysMaterialStamping 冲压库存管理
     * @return 冲压库存管理
     */
    @Override
    public List<SysMaterialStamping> selectSysMaterialStampingList(SysMaterialStamping sysMaterialStamping)
    {
        return sysMaterialStampingMapper.selectSysMaterialStampingList(sysMaterialStamping);
    }

    /**
     * 新增冲压库存管理
     * 
     * @param sysMaterialStamping 冲压库存管理
     * @return 结果
     */
    @Override
    public int insertSysMaterialStamping(SysMaterialStamping sysMaterialStamping)
    {
        return sysMaterialStampingMapper.insertSysMaterialStamping(sysMaterialStamping);
    }

    /**
     * 修改冲压库存管理
     * 
     * @param sysMaterialStamping 冲压库存管理
     * @return 结果
     */
    @Override
    public int updateSysMaterialStamping(SysMaterialStamping sysMaterialStamping)
    {
        return sysMaterialStampingMapper.updateSysMaterialStamping(sysMaterialStamping);
    }

    /**
     * 批量删除冲压库存管理
     * 
     * @param ids 需要删除的冲压库存管理主键
     * @return 结果
     */
    @Override
    public int deleteSysMaterialStampingByIds(Long[] ids)
    {
        return sysMaterialStampingMapper.deleteSysMaterialStampingByIds(ids);
    }

    /**
     * 删除冲压库存管理信息
     * 
     * @param id 冲压库存管理主键
     * @return 结果
     */
    @Override
    public int deleteSysMaterialStampingById(Long id)
    {
        return sysMaterialStampingMapper.deleteSysMaterialStampingById(id);
    }
}
