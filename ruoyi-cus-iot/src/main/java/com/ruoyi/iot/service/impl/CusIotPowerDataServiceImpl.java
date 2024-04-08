package com.ruoyi.iot.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.iot.mapper.CusIotPowerDataMapper;
import com.ruoyi.iot.domain.CusIotPowerData;
import com.ruoyi.iot.service.ICusIotPowerDataService;

/**
 * 功率数据Service业务层处理
 * 
 * @author Shawn
 * @date 2024-03-26
 */
@Service
public class CusIotPowerDataServiceImpl implements ICusIotPowerDataService 
{
    @Autowired
    private CusIotPowerDataMapper cusIotPowerDataMapper;

    /**
     * 查询功率数据
     * 
     * @param id 功率数据主键
     * @return 功率数据
     */
    @Override
    public CusIotPowerData selectCusIotPowerDataById(Long id)
    {
        return cusIotPowerDataMapper.selectCusIotPowerDataById(id);
    }

    /**
     * 查询功率数据列表
     * 
     * @param cusIotPowerData 功率数据
     * @return 功率数据
     */
    @Override
    public List<CusIotPowerData> selectCusIotPowerDataList(CusIotPowerData cusIotPowerData)
    {
        return cusIotPowerDataMapper.selectCusIotPowerDataList(cusIotPowerData);
    }

    /**
     *  精确查询功率数据列表
     *
     * @param cusIotPowerData 功率数据
     * @return 功率数据集合
     */
    public List<CusIotPowerData> selectCusIotPowerDataByField(CusIotPowerData cusIotPowerData){
        return cusIotPowerDataMapper.selectCusIotPowerDataByField(cusIotPowerData);
    }

    /**
     * 新增功率数据
     * 
     * @param cusIotPowerData 功率数据
     * @return 结果
     */
    @Override
    public int insertCusIotPowerData(CusIotPowerData cusIotPowerData)
    {
        return cusIotPowerDataMapper.insertCusIotPowerData(cusIotPowerData);
    }

    /**
     * 修改功率数据
     * 
     * @param cusIotPowerData 功率数据
     * @return 结果
     */
    @Override
    public int updateCusIotPowerData(CusIotPowerData cusIotPowerData)
    {
        return cusIotPowerDataMapper.updateCusIotPowerData(cusIotPowerData);
    }

    /**
     * 批量删除功率数据
     * 
     * @param ids 需要删除的功率数据主键
     * @return 结果
     */
    @Override
    public int deleteCusIotPowerDataByIds(Long[] ids)
    {
        return cusIotPowerDataMapper.deleteCusIotPowerDataByIds(ids);
    }

    /**
     * 删除功率数据信息
     * 
     * @param id 功率数据主键
     * @return 结果
     */
    @Override
    public int deleteCusIotPowerDataById(Long id)
    {
        return cusIotPowerDataMapper.deleteCusIotPowerDataById(id);
    }
}
