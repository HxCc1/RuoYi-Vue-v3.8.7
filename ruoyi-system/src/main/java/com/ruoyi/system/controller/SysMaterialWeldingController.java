package com.ruoyi.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysMaterialWelding;
import com.ruoyi.system.service.ISysMaterialWeldingService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 焊装物料库存管理Controller
 *
 * @author ruoyi
 * @date 2025-08-12
 */
@RestController
@RequestMapping("/system/weldingManage")
public class SysMaterialWeldingController extends BaseController
{
    @Autowired
    private ISysMaterialWeldingService sysMaterialWeldingService;

    /**
     * 查询焊装物料库存管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:weldingManage:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysMaterialWelding sysMaterialWelding)
    {
        startPage();
        List<SysMaterialWelding> list = sysMaterialWeldingService.selectSysMaterialWeldingList(sysMaterialWelding);
        return getDataTable(list);
    }

    /**
     * 导出焊装物料库存管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:weldingManage:export')")
    @Log(title = "焊装物料库存管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysMaterialWelding sysMaterialWelding)
    {
        List<SysMaterialWelding> list = sysMaterialWeldingService.selectSysMaterialWeldingList(sysMaterialWelding);
        ExcelUtil<SysMaterialWelding> util = new ExcelUtil<SysMaterialWelding>(SysMaterialWelding.class);
        util.exportExcel(response, list, "焊装物料库存管理数据");
    }

    /**
     * 获取焊装物料库存管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:weldingManage:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sysMaterialWeldingService.selectSysMaterialWeldingById(id));
    }

    /**
     * 新增焊装物料库存管理
     */
    @PreAuthorize("@ss.hasPermi('system:weldingManage:add')")
    @Log(title = "焊装物料库存管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysMaterialWelding sysMaterialWelding)
    {
        return toAjax(sysMaterialWeldingService.insertSysMaterialWelding(sysMaterialWelding));
    }

    /**
     * 修改焊装物料库存管理
     */
    @PreAuthorize("@ss.hasPermi('system:weldingManage:edit')")
    @Log(title = "焊装物料库存管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysMaterialWelding sysMaterialWelding)
    {
        return toAjax(sysMaterialWeldingService.updateSysMaterialWelding(sysMaterialWelding));
    }

    /**
     * 删除焊装物料库存管理
     */
    @PreAuthorize("@ss.hasPermi('system:weldingManage:remove')")
    @Log(title = "焊装物料库存管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysMaterialWeldingService.deleteSysMaterialWeldingByIds(ids));
    }

    /**
     * 转序涂装扣减库存
     */
    @PreAuthorize("@ss.hasPermi('system:weldingManage:transfer')")
    @Log(title = "焊装物料库存管理", businessType = BusinessType.UPDATE)
    @PostMapping("/transfer")
    public AjaxResult transfer(@RequestBody List<SysMaterialWelding> list) {
        if (CollectionUtils.isEmpty(list)) {
            return AjaxResult.error("转序数据不能为空");
        }
        return toAjax(sysMaterialWeldingService.transferStock(list));
    }

}
