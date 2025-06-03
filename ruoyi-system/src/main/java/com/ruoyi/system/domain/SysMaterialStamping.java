package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 冲压库存管理对象 sys_material_stamping
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
public class SysMaterialStamping extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 物料编码 */
    @Excel(name = "物料编码")
    private Long materialId;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String materialName;

    /** 供应商编码 */
    @Excel(name = "供应商编码")
    private Long supplierId;

    /** 供应商名称 */
    @Excel(name = "供应商名称")
    private String supplierName;

    /** 物料数量 */
    @Excel(name = "物料数量")
    private Long num;

    /** 物料价格 */
    @Excel(name = "物料价格")
    private BigDecimal price;

    /** 物料入库时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "物料入库时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date entryTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setMaterialId(Long materialId) 
    {
        this.materialId = materialId;
    }

    public Long getMaterialId() 
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
    public void setSupplierId(Long supplierId) 
    {
        this.supplierId = supplierId;
    }

    public Long getSupplierId() 
    {
        return supplierId;
    }
    public void setSupplierName(String supplierName) 
    {
        this.supplierName = supplierName;
    }

    public String getSupplierName() 
    {
        return supplierName;
    }
    public void setNum(Long num) 
    {
        this.num = num;
    }

    public Long getNum() 
    {
        return num;
    }
    public void setPrice(BigDecimal price) 
    {
        this.price = price;
    }

    public BigDecimal getPrice() 
    {
        return price;
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
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("materialId", getMaterialId())
            .append("materialName", getMaterialName())
            .append("supplierId", getSupplierId())
            .append("supplierName", getSupplierName())
            .append("num", getNum())
            .append("price", getPrice())
            .append("entryTime", getEntryTime())
            .append("remark", getRemark())
            .toString();
    }
}
