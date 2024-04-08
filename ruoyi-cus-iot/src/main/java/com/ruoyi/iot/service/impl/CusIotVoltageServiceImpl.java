package com.ruoyi.iot.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.iot.domain.CusIoTCurrent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.iot.mapper.CusIotVoltageMapper;
import com.ruoyi.iot.domain.CusIotVoltage;
import com.ruoyi.iot.service.ICusIotVoltageService;

/**
 * 电压数据Service业务层处理
 *
 * @author Shawn
 * @date 2024-03-26
 */
@Service
public class CusIotVoltageServiceImpl implements ICusIotVoltageService
{
    @Autowired
    private CusIotVoltageMapper cusIotVoltageMapper;

    /**
     * 查询电压数据
     *
     * @param id 电压数据主键
     * @return 电压数据
     */
    @Override
    public CusIotVoltage selectCusIotVoltageById(Long id)
    {
        return cusIotVoltageMapper.selectCusIotVoltageById(id);
    }

    /**
     * 查询最新一条电压数据
     *
     * @param deviceId 设备编码
     * @return 电压数据
     */
    @Override
    public CusIotVoltage selectCusIoTVoltageLatestOne(String deviceId)
    {
        return cusIotVoltageMapper.selectCusIoTVoltageLatestOne(deviceId);
    }

    /**
     * 查询电压数据列表
     *
     * @param cusIotVoltage 电压数据
     * @return 电压数据
     */
    @Override
    public List<CusIotVoltage> selectCusIotVoltageList(CusIotVoltage cusIotVoltage)
    {
        return cusIotVoltageMapper.selectCusIotVoltageList(cusIotVoltage);
    }

    /**
     * 精确获取电流数据详细信息
     * @param cusIotVoltage 电压数据
     * @return 电流数据集合
     */
    public List<CusIotVoltage> selectCusIoTVoltageByField(CusIotVoltage cusIotVoltage){
        return cusIotVoltageMapper.selectCusIoTVoltageByField(cusIotVoltage);
    }

    /**
     * 新增电压数据
     *
     * @param cusIotVoltage 电压数据
     * @return 结果
     */
    @Override
    public int insertCusIotVoltage(CusIotVoltage cusIotVoltage)
    {
        return cusIotVoltageMapper.insertCusIotVoltage(cusIotVoltage);
    }

    /**
     * 修改电压数据
     *
     * @param cusIotVoltage 电压数据
     * @return 结果
     */
    @Override
    public int updateCusIotVoltage(CusIotVoltage cusIotVoltage)
    {
        return cusIotVoltageMapper.updateCusIotVoltage(cusIotVoltage);
    }

    /**
     * 批量删除电压数据
     *
     * @param ids 需要删除的电压数据主键
     * @return 结果
     */
    @Override
    public int deleteCusIotVoltageByIds(Long[] ids)
    {
        return cusIotVoltageMapper.deleteCusIotVoltageByIds(ids);
    }

    /**
     * 删除电压数据信息
     *
     * @param id 电压数据主键
     * @return 结果
     */
    @Override
    public int deleteCusIotVoltageById(Long id)
    {
        return cusIotVoltageMapper.deleteCusIotVoltageById(id);
    }
}
