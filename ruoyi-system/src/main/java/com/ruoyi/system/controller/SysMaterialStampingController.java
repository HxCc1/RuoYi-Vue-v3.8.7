package com.ruoyi.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.ruoyi.system.domain.SysMaterialStamping;
import com.ruoyi.system.service.ISysMaterialStampingService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 冲压库存管理Controller
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
@RestController
@RequestMapping("/system/stamping")
public class SysMaterialStampingController extends BaseController
{
    @Autowired
    private ISysMaterialStampingService sysMaterialStampingService;

    /**
     * 查询冲压库存管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:stamping:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysMaterialStamping sysMaterialStamping)
    {
        startPage();
        List<SysMaterialStamping> list = sysMaterialStampingService.selectSysMaterialStampingList(sysMaterialStamping);
        return getDataTable(list);
    }

    /**
     * 导出冲压库存管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:stamping:export')")
    @Log(title = "冲压库存管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysMaterialStamping sysMaterialStamping)
    {
        List<SysMaterialStamping> list = sysMaterialStampingService.selectSysMaterialStampingList(sysMaterialStamping);
        ExcelUtil<SysMaterialStamping> util = new ExcelUtil<SysMaterialStamping>(SysMaterialStamping.class);
        util.exportExcel(response, list, "冲压库存管理数据");
    }

    /**
     * 获取冲压库存管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:stamping:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sysMaterialStampingService.selectSysMaterialStampingById(id));
    }

    /**
     * 新增冲压库存管理
     */
    @PreAuthorize("@ss.hasPermi('system:stamping:add')")
    @Log(title = "冲压库存管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysMaterialStamping sysMaterialStamping)
    {
        return toAjax(sysMaterialStampingService.insertSysMaterialStamping(sysMaterialStamping));
    }

    /**
     * 修改冲压库存管理
     */
    @PreAuthorize("@ss.hasPermi('system:stamping:edit')")
    @Log(title = "冲压库存管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysMaterialStamping sysMaterialStamping)
    {
        return toAjax(sysMaterialStampingService.updateSysMaterialStamping(sysMaterialStamping));
    }

    /**
     * 删除冲压库存管理
     */
    @PreAuthorize("@ss.hasPermi('system:stamping:remove')")
    @Log(title = "冲压库存管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysMaterialStampingService.deleteSysMaterialStampingByIds(ids));
    }
}
