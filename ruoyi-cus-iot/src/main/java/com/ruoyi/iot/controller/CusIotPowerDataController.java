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
import com.ruoyi.iot.domain.CusIotPowerData;
import com.ruoyi.iot.service.ICusIotPowerDataService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 功率数据Controller
 * 
 * @author Shawn
 * @date 2024-03-26
 */
@RestController
@RequestMapping("/IoT/powerData")
public class CusIotPowerDataController extends BaseController
{
    @Autowired
    private ICusIotPowerDataService cusIotPowerDataService;

    /**
     * 查询功率数据列表
     */
    @PreAuthorize("@ss.hasPermi('IoT:powerData:list')")
    @GetMapping("/list")
    public TableDataInfo list(CusIotPowerData cusIotPowerData)
    {
        startPage();
        List<CusIotPowerData> list = cusIotPowerDataService.selectCusIotPowerDataList(cusIotPowerData);
        return getDataTable(list);
    }

    /**
     * 精确查询功率数据列表
     */
    @PreAuthorize("@ss.hasPermi('IoT:powerData:list')")
    @GetMapping("/field")
    public TableDataInfo getPowerDataByField(CusIotPowerData cusIotPowerData)
    {
        List<CusIotPowerData> list = cusIotPowerDataService.selectCusIotPowerDataByField(cusIotPowerData);
        return getDataTable(list);
    }

    /**
     * 导出功率数据列表
     */
    @PreAuthorize("@ss.hasPermi('IoT:powerData:export')")
    @Log(title = "功率数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CusIotPowerData cusIotPowerData)
    {
        List<CusIotPowerData> list = cusIotPowerDataService.selectCusIotPowerDataList(cusIotPowerData);
        ExcelUtil<CusIotPowerData> util = new ExcelUtil<CusIotPowerData>(CusIotPowerData.class);
        util.exportExcel(response, list, "功率数据数据");
    }

    /**
     * 获取功率数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('IoT:powerData:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(cusIotPowerDataService.selectCusIotPowerDataById(id));
    }

    /**
     * 新增功率数据
     */
    @PreAuthorize("@ss.hasPermi('IoT:powerData:add')")
    @Log(title = "功率数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CusIotPowerData cusIotPowerData)
    {
        cusIotPowerData.setCreateBy(getUsername());
        return toAjax(cusIotPowerDataService.insertCusIotPowerData(cusIotPowerData));
    }

    /**
     * 修改功率数据
     */
    @PreAuthorize("@ss.hasPermi('IoT:powerData:edit')")
    @Log(title = "功率数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CusIotPowerData cusIotPowerData)
    {
        cusIotPowerData.setUpdateBy(getUsername());
        return toAjax(cusIotPowerDataService.updateCusIotPowerData(cusIotPowerData));
    }

    /**
     * 删除功率数据
     */
    @PreAuthorize("@ss.hasPermi('IoT:powerData:remove')")
    @Log(title = "功率数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cusIotPowerDataService.deleteCusIotPowerDataByIds(ids));
    }
}
