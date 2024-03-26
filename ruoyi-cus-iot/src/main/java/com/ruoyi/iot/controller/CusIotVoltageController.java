package com.ruoyi.iot.controller;

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
import com.ruoyi.iot.domain.CusIotVoltage;
import com.ruoyi.iot.service.ICusIotVoltageService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 电压数据Controller
 *
 * @author Shawn
 * @date 2024-03-26
 */
@RestController
@RequestMapping("/IoT/voltage")
public class CusIotVoltageController extends BaseController
{
    @Autowired
    private ICusIotVoltageService cusIotVoltageService;

    /**
     * 查询电压数据列表
     */
    @PreAuthorize("@ss.hasPermi('IoT:voltage:list')")
    @GetMapping("/list")
    public TableDataInfo list(CusIotVoltage cusIotVoltage)
    {
        startPage();
        List<CusIotVoltage> list = cusIotVoltageService.selectCusIotVoltageList(cusIotVoltage);
        return getDataTable(list);
    }

    /**
     * 导出电压数据列表
     */
    @PreAuthorize("@ss.hasPermi('IoT:voltage:export')")
    @Log(title = "电压数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CusIotVoltage cusIotVoltage)
    {
        List<CusIotVoltage> list = cusIotVoltageService.selectCusIotVoltageList(cusIotVoltage);
        ExcelUtil<CusIotVoltage> util = new ExcelUtil<CusIotVoltage>(CusIotVoltage.class);
        util.exportExcel(response, list, "电压数据数据");
    }

    /**
     * 获取电压数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('IoT:voltage:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(cusIotVoltageService.selectCusIotVoltageById(id));
    }

    /**
     * 新增电压数据
     */
    @PreAuthorize("@ss.hasPermi('IoT:voltage:add')")
    @Log(title = "电压数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CusIotVoltage cusIotVoltage)
    {
        cusIotVoltage.setCreateBy(getUsername());
        return toAjax(cusIotVoltageService.insertCusIotVoltage(cusIotVoltage));
    }

    /**
     * 修改电压数据
     */
    @PreAuthorize("@ss.hasPermi('IoT:voltage:edit')")
    @Log(title = "电压数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CusIotVoltage cusIotVoltage)
    {
        cusIotVoltage.setUpdateBy(getUsername());
        return toAjax(cusIotVoltageService.updateCusIotVoltage(cusIotVoltage));
    }

    /**
     * 删除电压数据
     */
    @PreAuthorize("@ss.hasPermi('IoT:voltage:remove')")
    @Log(title = "电压数据", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cusIotVoltageService.deleteCusIotVoltageByIds(ids));
    }
}
