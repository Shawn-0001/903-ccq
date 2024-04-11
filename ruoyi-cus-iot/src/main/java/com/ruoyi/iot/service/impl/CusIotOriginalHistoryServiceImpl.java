package com.ruoyi.iot.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.iot.mapper.CusIotOriginalHistoryMapper;
import com.ruoyi.iot.domain.CusIotOriginalHistory;
import com.ruoyi.iot.service.ICusIotOriginalHistoryService;

/**
 * 原始数据历史Service业务层处理
 *
 * @author Shawn
 * @date 2024-04-11
 */
@Service
public class CusIotOriginalHistoryServiceImpl implements ICusIotOriginalHistoryService
{
    @Autowired
    private CusIotOriginalHistoryMapper cusIotOriginalHistoryMapper;

    /**
     * 查询原始数据历史
     *
     * @param id 原始数据历史主键
     * @return 原始数据历史
     */
    @Override
    public CusIotOriginalHistory selectCusIotOriginalHistoryById(Long id)
    {
        return cusIotOriginalHistoryMapper.selectCusIotOriginalHistoryById(id);
    }

    /**
     * 查询原始数据历史列表
     *
     * @param cusIotOriginalHistory 原始数据历史
     * @return 原始数据历史
     */
    @Override
    public List<CusIotOriginalHistory> selectCusIotOriginalHistoryList(CusIotOriginalHistory cusIotOriginalHistory)
    {
        return cusIotOriginalHistoryMapper.selectCusIotOriginalHistoryList(cusIotOriginalHistory);
    }

    /**
     * 新增原始数据历史
     *
     * @param cusIotOriginalHistory 原始数据历史
     * @return 结果
     */
    @Override
    public int insertCusIotOriginalHistory(CusIotOriginalHistory cusIotOriginalHistory)
    {
        return cusIotOriginalHistoryMapper.insertCusIotOriginalHistory(cusIotOriginalHistory);
    }

    /**
     * 修改原始数据历史
     *
     * @param cusIotOriginalHistory 原始数据历史
     * @return 结果
     */
    @Override
    public int updateCusIotOriginalHistory(CusIotOriginalHistory cusIotOriginalHistory)
    {
        return cusIotOriginalHistoryMapper.updateCusIotOriginalHistory(cusIotOriginalHistory);
    }

    /**
     * 批量删除原始数据历史
     *
     * @param ids 需要删除的原始数据历史主键
     * @return 结果
     */
    @Override
    public int deleteCusIotOriginalHistoryByIds(Long[] ids)
    {
        return cusIotOriginalHistoryMapper.deleteCusIotOriginalHistoryByIds(ids);
    }

    /**
     * 删除原始数据历史信息
     *
     * @param id 原始数据历史主键
     * @return 结果
     */
    @Override
    public int deleteCusIotOriginalHistoryById(Long id)
    {
        return cusIotOriginalHistoryMapper.deleteCusIotOriginalHistoryById(id);
    }
}
