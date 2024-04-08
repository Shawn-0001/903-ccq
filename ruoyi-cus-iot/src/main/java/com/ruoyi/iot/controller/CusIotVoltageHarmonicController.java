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
import com.ruoyi.iot.domain.CusIotVoltageHarmonic;
import com.ruoyi.iot.service.ICusIotVoltageHarmonicService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 谐波电压数据Controller
 * 
 * @author Shawn
 * @date 2024-03-26
 */
@RestController
@RequestMapping("/IoT/harmonicVoltage")
public class CusIotVoltageHarmonicController extends BaseController
{
    @Autowired
    private ICusIotVoltageHarmonicService cusIotVoltageHarmonicService;

    /**
     * 查询谐波电压数据列表
     */
    @PreAuthorize("@ss.hasPermi('IoT:harmonicVoltage:list')")
    @GetMapping("/list")
    public TableDataInfo list(CusIotVoltageHarmonic cusIotVoltageHarmonic)
    {
        startPage();
        List<CusIotVoltageHarmonic> list = cusIotVoltageHarmonicService.selectCusIotVoltageHarmonicList(cusIotVoltageHarmonic);
        return getDataTable(list);
    }

    /**
     * 查询最新一条谐波电压数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('IoT:harmonicVoltage:query')")
    @GetMapping("/latest/{deviceId}")
    public AjaxResult getLatestOne(@PathVariable("deviceId") String deviceId) {
        return success(cusIotVoltageHarmonicService.selectCusIoTVoltageHarmonicLatestOne(deviceId));
    }

    /**
     * 导出谐波电压数据列表
     */
    @PreAuthorize("@ss.hasPermi('IoT:harmonicVoltage:export')")
    @Log(title = "谐波电压数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CusIotVoltageHarmonic cusIotVoltageHarmonic)
    {
        List<CusIotVoltageHarmonic> list = cusIotVoltageHarmonicService.selectCusIotVoltageHarmonicList(cusIotVoltageHarmonic);
        ExcelUtil<CusIotVoltageHarmonic> util = new ExcelUtil<CusIotVoltageHarmonic>(CusIotVoltageHarmonic.class);
        util.exportExcel(response, list, "谐波电压数据数据");
    }

    /**
     * 获取谐波电压数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('IoT:harmonicVoltage:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(cusIotVoltageHarmonicService.selectCusIotVoltageHarmonicById(id));
    }

    /**
     * 新增谐波电压数据
     */
    @PreAuthorize("@ss.hasPermi('IoT:harmonicVoltage:add')")
    @Log(title = "谐波电压数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CusIotVoltageHarmonic cusIotVoltageHarmonic)
    {
        cusIotVoltageHarmonic.setCreateBy(getUsername());
        return toAjax(cusIotVoltageHarmonicService.insertCusIotVoltageHarmonic(cusIotVoltageHarmonic));
    }

    /**
     * 修改谐波电压数据
     */
    @PreAuthorize("@ss.hasPermi('IoT:harmonicVoltage:edit')")
    @Log(title = "谐波电压数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CusIotVoltageHarmonic cusIotVoltageHarmonic)
    {
        cusIotVoltageHarmonic.setUpdateBy(getUsername());
        return toAjax(cusIotVoltageHarmonicService.updateCusIotVoltageHarmonic(cusIotVoltageHarmonic));
    }

    /**
     * 删除谐波电压数据
     */
    @PreAuthorize("@ss.hasPermi('IoT:harmonicVoltage:remove')")
    @Log(title = "谐波电压数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cusIotVoltageHarmonicService.deleteCusIotVoltageHarmonicByIds(ids));
    }
}
