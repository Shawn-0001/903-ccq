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
import com.ruoyi.iot.domain.CusIotOriginalHistory;
import com.ruoyi.iot.service.ICusIotOriginalHistoryService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 原始数据历史Controller
 *
 * @author Shawn
 * @date 2024-04-11
 */
@RestController
@RequestMapping("/IoT/originalHistory")
public class CusIotOriginalHistoryController extends BaseController
{
    @Autowired
    private ICusIotOriginalHistoryService cusIotOriginalHistoryService;

    /**
     * 查询原始数据历史列表
     */
    @PreAuthorize("@ss.hasPermi('IoT:originalHistory:list')")
    @GetMapping("/list")
    public TableDataInfo list(CusIotOriginalHistory cusIotOriginalHistory)
    {
        startPage();
        List<CusIotOriginalHistory> list = cusIotOriginalHistoryService.selectCusIotOriginalHistoryList(cusIotOriginalHistory);
        return getDataTable(list);
    }

    /**
     * 导出原始数据历史列表
     */
    @PreAuthorize("@ss.hasPermi('IoT:originalHistory:export')")
    @Log(title = "原始数据历史", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CusIotOriginalHistory cusIotOriginalHistory)
    {
        List<CusIotOriginalHistory> list = cusIotOriginalHistoryService.selectCusIotOriginalHistoryList(cusIotOriginalHistory);
        ExcelUtil<CusIotOriginalHistory> util = new ExcelUtil<CusIotOriginalHistory>(CusIotOriginalHistory.class);
        util.exportExcel(response, list, "原始数据历史数据");
    }

    /**
     * 获取原始数据历史详细信息
     */
    @PreAuthorize("@ss.hasPermi('IoT:originalHistory:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(cusIotOriginalHistoryService.selectCusIotOriginalHistoryById(id));
    }

    /**
     * 新增原始数据历史
     */
    @PreAuthorize("@ss.hasPermi('IoT:originalHistory:add')")
    @Log(title = "原始数据历史", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CusIotOriginalHistory cusIotOriginalHistory)
    {
        cusIotOriginalHistory.setCreateBy(getUsername());
        return toAjax(cusIotOriginalHistoryService.insertCusIotOriginalHistory(cusIotOriginalHistory));
    }

    /**
     * 修改原始数据历史
     */
    @PreAuthorize("@ss.hasPermi('IoT:originalHistory:edit')")
    @Log(title = "原始数据历史", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CusIotOriginalHistory cusIotOriginalHistory)
    {
        cusIotOriginalHistory.setUpdateBy(getUsername());
        return toAjax(cusIotOriginalHistoryService.updateCusIotOriginalHistory(cusIotOriginalHistory));
    }

    /**
     * 删除原始数据历史
     */
    @PreAuthorize("@ss.hasPermi('IoT:originalHistory:remove')")
    @Log(title = "原始数据历史", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cusIotOriginalHistoryService.deleteCusIotOriginalHistoryByIds(ids));
    }
}
