package com.ruoyi.iot.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.SecurityUtils;
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
import com.ruoyi.iot.domain.CusIoTCurrent;
import com.ruoyi.iot.service.ICusIoTCurrentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 电流数据Controller
 *
 * @author Shawn
 * @date 2024-03-25
 */
@RestController
@RequestMapping("/IoT/current")
public class CusIoTCurrentController extends BaseController
{
    @Autowired
    private ICusIoTCurrentService cusIoTCurrentService;

    /**
     * 查询电流数据列表
     */
    @PreAuthorize("@ss.hasPermi('IoT:current:list')")
    @GetMapping("/list")
    public TableDataInfo list(CusIoTCurrent cusIoTCurrent)
    {
        startPage();
        List<CusIoTCurrent> list = cusIoTCurrentService.selectCusIoTCurrentList(cusIoTCurrent);
        return getDataTable(list);
    }

    /**
     * 导出电流数据列表
     */
    @PreAuthorize("@ss.hasPermi('IoT:current:export')")
    @Log(title = "电流数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CusIoTCurrent cusIoTCurrent)
    {
        List<CusIoTCurrent> list = cusIoTCurrentService.selectCusIoTCurrentList(cusIoTCurrent);
        ExcelUtil<CusIoTCurrent> util = new ExcelUtil<CusIoTCurrent>(CusIoTCurrent.class);
        util.exportExcel(response, list, "电流数据数据");
    }

    /**
     * 获取电流数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('IoT:current:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(cusIoTCurrentService.selectCusIoTCurrentById(id));
    }

    /**
     * 新增电流数据
     */
    @PreAuthorize("@ss.hasPermi('IoT:current:add')")
    @Log(title = "电流数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CusIoTCurrent cusIoTCurrent)
    {
        cusIoTCurrent.setCreateBy(getUsername());
        return toAjax(cusIoTCurrentService.insertCusIoTCurrent(cusIoTCurrent));
    }

    /**
     * 修改电流数据
     */
    @PreAuthorize("@ss.hasPermi('IoT:current:edit')")
    @Log(title = "电流数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CusIoTCurrent cusIoTCurrent)
    {
        cusIoTCurrent.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(cusIoTCurrentService.updateCusIoTCurrent(cusIoTCurrent));
    }

    /**
     * 删除电流数据
     */
    @PreAuthorize("@ss.hasPermi('IoT:current:remove')")
    @Log(title = "电流数据", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cusIoTCurrentService.deleteCusIoTCurrentByIds(ids));
    }
}
