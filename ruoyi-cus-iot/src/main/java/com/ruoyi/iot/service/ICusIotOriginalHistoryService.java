package com.ruoyi.iot.service;

import java.util.List;
import com.ruoyi.iot.domain.CusIotOriginalHistory;

/**
 * 原始数据历史Service接口
 *
 * @author Shawn
 * @date 2024-04-11
 */
public interface ICusIotOriginalHistoryService
{
    /**
     * 查询原始数据历史
     *
     * @param id 原始数据历史主键
     * @return 原始数据历史
     */
    public CusIotOriginalHistory selectCusIotOriginalHistoryById(Long id);

    /**
     * 查询原始数据历史列表
     *
     * @param cusIotOriginalHistory 原始数据历史
     * @return 原始数据历史集合
     */
    public List<CusIotOriginalHistory> selectCusIotOriginalHistoryList(CusIotOriginalHistory cusIotOriginalHistory);

    /**
     * 新增原始数据历史
     *
     * @param cusIotOriginalHistory 原始数据历史
     * @return 结果
     */
    public int insertCusIotOriginalHistory(CusIotOriginalHistory cusIotOriginalHistory);

    /**
     * 修改原始数据历史
     *
     * @param cusIotOriginalHistory 原始数据历史
     * @return 结果
     */
    public int updateCusIotOriginalHistory(CusIotOriginalHistory cusIotOriginalHistory);

    /**
     * 批量删除原始数据历史
     *
     * @param ids 需要删除的原始数据历史主键集合
     * @return 结果
     */
    public int deleteCusIotOriginalHistoryByIds(Long[] ids);

    /**
     * 删除原始数据历史信息
     *
     * @param id 原始数据历史主键
     * @return 结果
     */
    public int deleteCusIotOriginalHistoryById(Long id);
}
