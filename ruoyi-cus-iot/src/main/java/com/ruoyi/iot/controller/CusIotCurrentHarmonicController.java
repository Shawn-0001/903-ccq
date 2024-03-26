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
import com.ruoyi.iot.domain.CusIotCurrentHarmonic;
import com.ruoyi.iot.service.ICusIotCurrentHarmonicService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 谐波电流数据Controller
 * 
 * @author Shawn
 * @date 2024-03-26
 */
@RestController
@RequestMapping("/IoT/harmonicCurrent")
public class CusIotCurrentHarmonicController extends BaseController
{
    @Autowired
    private ICusIotCurrentHarmonicService cusIotCurrentHarmonicService;

    /**
     * 查询谐波电流数据列表
     */
    @PreAuthorize("@ss.hasPermi('IoT:harmonicCurrent:list')")
    @GetMapping("/list")
    public TableDataInfo list(CusIotCurrentHarmonic cusIotCurrentHarmonic)
    {
        startPage();
        List<CusIotCurrentHarmonic> list = cusIotCurrentHarmonicService.selectCusIotCurrentHarmonicList(cusIotCurrentHarmonic);
        return getDataTable(list);
    }

    /**
     * 导出谐波电流数据列表
     */
    @PreAuthorize("@ss.hasPermi('IoT:harmonicCurrent:export')")
    @Log(title = "谐波电流数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CusIotCurrentHarmonic cusIotCurrentHarmonic)
    {
        List<CusIotCurrentHarmonic> list = cusIotCurrentHarmonicService.selectCusIotCurrentHarmonicList(cusIotCurrentHarmonic);
        ExcelUtil<CusIotCurrentHarmonic> util = new ExcelUtil<CusIotCurrentHarmonic>(CusIotCurrentHarmonic.class);
        util.exportExcel(response, list, "谐波电流数据数据");
    }

    /**
     * 获取谐波电流数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('IoT:harmonicCurrent:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(cusIotCurrentHarmonicService.selectCusIotCurrentHarmonicById(id));
    }

    /**
     * 新增谐波电流数据
     */
    @PreAuthorize("@ss.hasPermi('IoT:harmonicCurrent:add')")
    @Log(title = "谐波电流数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CusIotCurrentHarmonic cusIotCurrentHarmonic)
    {
        cusIotCurrentHarmonic.setCreateBy(getUsername());
        return toAjax(cusIotCurrentHarmonicService.insertCusIotCurrentHarmonic(cusIotCurrentHarmonic));
    }

    /**
     * 修改谐波电流数据
     */
    @PreAuthorize("@ss.hasPermi('IoT:harmonicCurrent:edit')")
    @Log(title = "谐波电流数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CusIotCurrentHarmonic cusIotCurrentHarmonic)
    {
        cusIotCurrentHarmonic.setUpdateBy(getUsername());
        return toAjax(cusIotCurrentHarmonicService.updateCusIotCurrentHarmonic(cusIotCurrentHarmonic));
    }

    /**
     * 删除谐波电流数据
     */
    @PreAuthorize("@ss.hasPermi('IoT:harmonicCurrent:remove')")
    @Log(title = "谐波电流数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cusIotCurrentHarmonicService.deleteCusIotCurrentHarmonicByIds(ids));
    }
}
