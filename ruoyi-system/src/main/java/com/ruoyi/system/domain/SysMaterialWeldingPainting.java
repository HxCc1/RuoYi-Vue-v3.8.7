package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;

import java.util.Date;

public class SysMaterialWeldingPainting {

    // 用于序列化的版本控制，表示该类的序列化版本号为 1L
    private static final long serialVersionUID = 1L;

    /** 物料编码 */
    private String materialId;

    /** 物料名称 */
    private String materialName;

    /** 数量 */
    private int num;

    /** 转序日期 */
    private Date procedingDate;

    /** 操作人 */
    private String operator;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Date getProcedingDate() {
        return procedingDate;
    }

    public void setProcedingDate(Date procedingDate) {
        this.procedingDate = procedingDate;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "sys_material_welding_painting{" +
                "materialId='" + materialId + '\'' +
                ", materialName='" + materialName + '\'' +
                ", num=" + num +
                ", procedingDate=" + procedingDate +
                ", operator='" + operator + '\'' +
                '}';
    }
}
