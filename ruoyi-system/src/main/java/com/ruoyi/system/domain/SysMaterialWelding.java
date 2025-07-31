package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 总成件库存管理对象 sys_material_welding
 *
 * @author ruoyi
 * @date 2025-06-05
 */
public class SysMaterialWelding extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 物料编码 */
    @Excel(name = "物料编码")
    private String materialId;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String materialName;

    /** 数量 */
    @Excel(name = "数量")
    private int num;

    /** 入库时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入库时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date entryTime;

    /** 备注 */
    private String remark;

    /** 报工状态 */
    private int syncFlag;

    public void setId(Long id)
    {
        this.id = id;
    }
    public Long getId()
    {
        return id;
    }

    public void setMaterialId(String materialId)
    {
        this.materialId = materialId;
    }
    public String getMaterialId()
    {
        return materialId;
    }

    public void setMaterialName(String materialName)
    {
        this.materialName = materialName;
    }
    public String getMaterialName()
    {
        return materialName;
    }

    public void setNum(int num)
    {
        this.num = num;
    }
    public int getNum()
    {
        return num;
    }

    public void setEntryTime(Date entryTime)
    {
        this.entryTime = entryTime;
    }
    public Date getEntryTime()
    {
        return entryTime;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSyncFlag() {
        return syncFlag;
    }
    public void setSyncFlag(int syncFlag) {
        this.syncFlag = syncFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("materialId", getMaterialId())
                .append("materialName", getMaterialName())
                .append("num", getNum())
                .append("entryTime", getEntryTime())
                .append("remark", getRemark())
                .append("syncFlag",getSyncFlag())
                .toString();
    }
}
