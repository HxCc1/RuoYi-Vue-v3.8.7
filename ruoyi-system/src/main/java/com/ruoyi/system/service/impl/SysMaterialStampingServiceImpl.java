package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysMaterialStampingMapper;
import com.ruoyi.system.domain.SysMaterialStamping;
import com.ruoyi.system.service.ISysMaterialStampingService;

/**
 * 冲压件库存管理Service业务层处理
 *
 * @author ruoyi
 * @date 2025-06-04
 */
@Service
public class SysMaterialStampingServiceImpl implements ISysMaterialStampingService
{
    @Autowired
    private SysMaterialStampingMapper sysMaterialStampingMapper;

    /**
     * 查询冲压件库存管理
     *
     * @param id 冲压件库存管理主键
     * @return 冲压件库存管理
     */
    @Override
    public SysMaterialStamping selectSysMaterialStampingById(Long id)
    {
        return sysMaterialStampingMapper.selectSysMaterialStampingById(id);
    }

    /**
     * 查询冲压件库存管理列表
     *
     * @param sysMaterialStamping 冲压件库存管理
     * @return 冲压件库存管理
     */
    @Override
    public List<SysMaterialStamping> selectSysMaterialStampingList(SysMaterialStamping sysMaterialStamping)
    {
        return sysMaterialStampingMapper.selectSysMaterialStampingList(sysMaterialStamping);
    }

    /**
     * 新增冲压件库存管理
     *
     * @param sysMaterialStamping 冲压件库存管理
     * @return 结果
     */
    @Override
    public int insertSysMaterialStamping(SysMaterialStamping sysMaterialStamping)
    {
        return sysMaterialStampingMapper.insertSysMaterialStamping(sysMaterialStamping);
    }

    /**
     * 修改冲压件库存管理
     *
     * @param sysMaterialStamping 冲压件库存管理
     * @return 结果
     */
    @Override
    public int updateSysMaterialStamping(SysMaterialStamping sysMaterialStamping)
    {
        return sysMaterialStampingMapper.updateSysMaterialStamping(sysMaterialStamping);
    }

    /**
     * 批量删除冲压件库存管理
     *
     * @param ids 需要删除的冲压件库存管理主键
     * @return 结果
     */
    @Override
    public int deleteSysMaterialStampingByIds(Long[] ids)
    {
        return sysMaterialStampingMapper.deleteSysMaterialStampingByIds(ids);
    }

    /**
     * 删除冲压件库存管理信息
     *
     * @param id 冲压件库存管理主键
     * @return 结果
     */
    @Override
    public int deleteSysMaterialStampingById(Long id)
    {
        return sysMaterialStampingMapper.deleteSysMaterialStampingById(id);
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
    public String importStamping(List<SysMaterialStamping> stampingList, boolean updateSupport, String operName) {
        // 这里可以添加具体的导入逻辑，例如循环插入或更新数据
        for (SysMaterialStamping stamping : stampingList) {
            if (updateSupport) {
                // 如果支持更新，先尝试更新，更新失败则插入
                int updateResult = sysMaterialStampingMapper.updateSysMaterialStamping(stamping);
                if (updateResult == 0) {
                    sysMaterialStampingMapper.insertSysMaterialStamping(stamping);
                }
            } else {
                // 不支持更新，直接插入
                sysMaterialStampingMapper.insertSysMaterialStamping(stamping);
            }
        }
        return "导入成功";
    }

}
