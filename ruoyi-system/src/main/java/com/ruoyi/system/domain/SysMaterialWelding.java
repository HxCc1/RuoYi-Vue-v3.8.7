package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 焊装物料库存管理对象 sys_material_welding
 *
 * @author ruoyi
 * @date 2025-08-12
 */
public class SysMaterialWelding extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 总成件物料编码 */
    @Excel(name = "总成件物料编码")
    private String materialId;

    /** 总成件物料名称 */
    @Excel(name = "总成件物料名称")
    private String materialName;

    /** 数量 */
    @Excel(name = "数量")
    private Long num;

    /** 转序日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "转序日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date procedingDate;

    /** 报工操作人 */
    @Excel(name = "报工操作人")
    private String operator;

    /** 创建日期 */
    private Date createDatetime;

    /** 更新日期 */
    private Date updateDatetime;

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
    public void setNum(Long num)
    {
        this.num = num;
    }

    public Long getNum()
    {
        return num;
    }
    public void setProcedingDate(Date procedingDate)
    {
        this.procedingDate = procedingDate;
    }

    public Date getProcedingDate()
    {
        return procedingDate;
    }
    public void setOperator(String operator)
    {
        this.operator = operator;
    }

    public String getOperator()
    {
        return operator;
    }
    public void setCreateDatetime(Date createDatetime)
    {
        this.createDatetime = createDatetime;
    }

    public Date getCreateDatetime()
    {
        return createDatetime;
    }
    public void setUpdateDatetime(Date updateDatetime)
    {
        this.updateDatetime = updateDatetime;
    }

    public Date getUpdateDatetime()
    {
        return updateDatetime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("materialId", getMaterialId())
                .append("materialName", getMaterialName())
                .append("num", getNum())
                .append("procedingDate", getProcedingDate())
                .append("operator", getOperator())
                .append("createDatetime", getCreateDatetime())
                .append("updateDatetime", getUpdateDatetime())
                .toString();
    }
}
