package com.ruoyi.iot.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.iot.domain.CusIoTCurrent;
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
import com.ruoyi.iot.domain.CusIotDeviceList;
import com.ruoyi.iot.service.ICusIotDeviceListService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 设备列表Controller
 *
 * @author Shawn
 * @date 2024-04-01
 */
@RestController
@RequestMapping("/IoT/deviceList")
public class CusIotDeviceListController extends BaseController
{
    @Autowired
    private ICusIotDeviceListService cusIotDeviceListService;

    /**
     * 查询设备列表
     */
    @PreAuthorize("@ss.hasPermi('IoT:deviceList:list')")
    @GetMapping("/list")
    public TableDataInfo list(CusIotDeviceList cusIotDeviceList)
    {
        startPage();
        List<CusIotDeviceList> list = cusIotDeviceListService.selectCusIotDeviceListList(cusIotDeviceList);
        return getDataTable(list);
    }

    /**
     *  精确查询设备列表
     */
    @PreAuthorize("@ss.hasPermi('IoT:deviceList:list')")
    @GetMapping(value = "/field")
    public TableDataInfo getDeviceByField(CusIotDeviceList cusIotDeviceList) {
        List<CusIotDeviceList> list = cusIotDeviceListService.selectCusIoTDeviceByField(cusIotDeviceList);
        return getDataTable(list);
    }

    /**
     * 导出设备列表列表
     */
    @PreAuthorize("@ss.hasPermi('IoT:deviceList:export')")
    @Log(title = "设备列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CusIotDeviceList cusIotDeviceList)
    {
        List<CusIotDeviceList> list = cusIotDeviceListService.selectCusIotDeviceListList(cusIotDeviceList);
        ExcelUtil<CusIotDeviceList> util = new ExcelUtil<CusIotDeviceList>(CusIotDeviceList.class);
        util.exportExcel(response, list, "设备列表数据");
    }

    /**
     * 获取设备列表详细信息
     */
    @PreAuthorize("@ss.hasPermi('IoT:deviceList:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(cusIotDeviceListService.selectCusIotDeviceListById(id));
    }

    /**
     * 新增设备列表
     */
    @PreAuthorize("@ss.hasPermi('IoT:deviceList:add')")
    @Log(title = "设备列表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CusIotDeviceList cusIotDeviceList)
    {
        cusIotDeviceList.setCreateBy(getUsername());
        return toAjax(cusIotDeviceListService.insertCusIotDeviceList(cusIotDeviceList));
    }

    /**
     * 修改设备列表
     */
    @PreAuthorize("@ss.hasPermi('IoT:deviceList:edit')")
    @Log(title = "设备列表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CusIotDeviceList cusIotDeviceList)
    {
        cusIotDeviceList.setUpdateBy(getUsername());
        return toAjax(cusIotDeviceListService.updateCusIotDeviceList(cusIotDeviceList));
    }

    /**
     * 删除设备列表
     */
    @PreAuthorize("@ss.hasPermi('IoT:deviceList:remove')")
    @Log(title = "设备列表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cusIotDeviceListService.deleteCusIotDeviceListByIds(ids));
    }
}
