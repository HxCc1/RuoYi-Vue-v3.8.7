package com.ruoyi.system.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysMaterialWelding;
import com.ruoyi.system.service.ISysMaterialWeldingService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.system.util.WebServiceUtils;

/**
 * 总成件库存管理Controller
 *
 * @author ruoyi
 * @date 2025-06-05
 */
@RestController
@RequestMapping("/system/welding")
public class SysMaterialWeldingController extends BaseController
{
    @Autowired
    private ISysMaterialWeldingService sysMaterialWeldingService;

    /**
     * 查询总成件库存管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:welding:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysMaterialWelding sysMaterialWelding)
    {
        startPage();
        List<SysMaterialWelding> list = sysMaterialWeldingService.selectSysMaterialWeldingList(sysMaterialWelding);
        return getDataTable(list);
    }

    /**
     * 导出总成件库存管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:welding:export')")
    @Log(title = "总成件库存管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysMaterialWelding sysMaterialWelding)
    {
        List<SysMaterialWelding> list = sysMaterialWeldingService.selectSysMaterialWeldingList(sysMaterialWelding);
        ExcelUtil<SysMaterialWelding> util = new ExcelUtil<SysMaterialWelding>(SysMaterialWelding.class);
        util.exportExcel(response, list, "总成件库存管理数据");
    }

    /**
     * 获取总成件库存管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:welding:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sysMaterialWeldingService.selectSysMaterialWeldingById(id));
    }

    /**
     * 新增总成件库存管理
     */
    @PreAuthorize("@ss.hasPermi('system:welding:add')")
    @Log(title = "总成件库存管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysMaterialWelding sysMaterialWelding)
    {
        return toAjax(sysMaterialWeldingService.insertSysMaterialWelding(sysMaterialWelding));
    }

    /**
     * 修改总成件库存管理
     */
    @PreAuthorize("@ss.hasPermi('system:welding:edit')")
    @Log(title = "总成件库存管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysMaterialWelding sysMaterialWelding)
    {
        return toAjax(sysMaterialWeldingService.updateSysMaterialWelding(sysMaterialWelding));
    }

    /**
     * 删除总成件库存管理
     */
    @PreAuthorize("@ss.hasPermi('system:welding:remove')")
    @Log(title = "总成件库存管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysMaterialWeldingService.deleteSysMaterialWeldingByIds(ids));
    }

    /**
     * 导入冲压库存管理数据
     */
    @PreAuthorize("@ss.hasPermi('system:welding:import')")
    @Log(title = "冲压库存管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(@RequestParam("file") MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<SysMaterialWelding> util = new ExcelUtil<SysMaterialWelding>(SysMaterialWelding.class);
        List<SysMaterialWelding> stampingList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = sysMaterialWeldingService.importWelding(stampingList, updateSupport, operName);
        return success(message);
    }

    /**
     * 导入excel模板下载
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<SysMaterialWelding> util = new ExcelUtil<SysMaterialWelding>(SysMaterialWelding.class);
        util.importTemplateExcel(response, "物料数据");
    }

    /**
     * 报工冲压库存管理数据
     */
    @PreAuthorize("@ss.hasPermi('system:welding:syncStamping')")
    @Log(title = "冲压库存管理", businessType = BusinessType.IMPORT)
    @PostMapping("/syncStamping")
    public AjaxResult syncStampingMateria(@RequestBody List<SysMaterialWelding> materialList) {

        if (materialList == null || materialList.isEmpty()) {
            throw new IllegalArgumentException("未选择需要报工的记录");
        }

        // 幂等性校验
        // 提取待同步记录的ID列表
        List<Long> ids = materialList.stream()
                .map(SysMaterialWelding::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (ids.isEmpty()) {
            return AjaxResult.error("选中的记录中不包含有效ID");
        }

        // 查询已同步的记录ID
        List<Long> syncedIds = sysMaterialWeldingService.selectSyncedIds(ids);
        // 不为空
        if (!syncedIds.isEmpty()) {
            // 构建错误提示信息（限制显示数量）
            String syncedIdStr = syncedIds.stream()
                    .limit(5)
                    .map(String::valueOf)
                    .collect(Collectors.joining("、"));
            String message = syncedIds.size() <= 5
                    ? "记录ID[" + syncedIdStr + "]已同步，不可重复报工"
                    : "记录ID[" + syncedIdStr + "等]已同步，不可重复报工";
            return AjaxResult.error(message);
        }
        // 调用BOM接口，计算报工数量
        return success(sysMaterialWeldingService.syncStampingByMateriaId(materialList));

    }

}
