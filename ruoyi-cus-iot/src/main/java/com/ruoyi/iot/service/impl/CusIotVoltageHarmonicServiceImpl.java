package com.ruoyi.iot.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.iot.mapper.CusIotVoltageHarmonicMapper;
import com.ruoyi.iot.domain.CusIotVoltageHarmonic;
import com.ruoyi.iot.service.ICusIotVoltageHarmonicService;

/**
 * 谐波电压数据Service业务层处理
 * 
 * @author Shawn
 * @date 2024-03-26
 */
@Service
public class CusIotVoltageHarmonicServiceImpl implements ICusIotVoltageHarmonicService 
{
    @Autowired
    private CusIotVoltageHarmonicMapper cusIotVoltageHarmonicMapper;

    /**
     * 查询谐波电压数据
     * 
     * @param id 谐波电压数据主键
     * @return 谐波电压数据
     */
    @Override
    public CusIotVoltageHarmonic selectCusIotVoltageHarmonicById(Long id)
    {
        return cusIotVoltageHarmonicMapper.selectCusIotVoltageHarmonicById(id);
    }

    /**
     * 查询谐波电压数据列表
     * 
     * @param cusIotVoltageHarmonic 谐波电压数据
     * @return 谐波电压数据
     */
    @Override
    public List<CusIotVoltageHarmonic> selectCusIotVoltageHarmonicList(CusIotVoltageHarmonic cusIotVoltageHarmonic)
    {
        return cusIotVoltageHarmonicMapper.selectCusIotVoltageHarmonicList(cusIotVoltageHarmonic);
    }

    /**
     * 查询最新一条谐波电压数据详细信息
     *
     * @param deviceId 谐波电压数据主键
     * @return 谐波电压数据
     */
    public CusIotVoltageHarmonic selectCusIoTVoltageHarmonicLatestOne(String deviceId){
        return cusIotVoltageHarmonicMapper.selectCusIoTVoltageHarmonicLatestOne(deviceId);
    }

    /**
     * 新增谐波电压数据
     * 
     * @param cusIotVoltageHarmonic 谐波电压数据
     * @return 结果
     */
    @Override
    public int insertCusIotVoltageHarmonic(CusIotVoltageHarmonic cusIotVoltageHarmonic)
    {
        return cusIotVoltageHarmonicMapper.insertCusIotVoltageHarmonic(cusIotVoltageHarmonic);
    }

    /**
     * 修改谐波电压数据
     * 
     * @param cusIotVoltageHarmonic 谐波电压数据
     * @return 结果
     */
    @Override
    public int updateCusIotVoltageHarmonic(CusIotVoltageHarmonic cusIotVoltageHarmonic)
    {
        return cusIotVoltageHarmonicMapper.updateCusIotVoltageHarmonic(cusIotVoltageHarmonic);
    }

    /**
     * 批量删除谐波电压数据
     * 
     * @param ids 需要删除的谐波电压数据主键
     * @return 结果
     */
    @Override
    public int deleteCusIotVoltageHarmonicByIds(Long[] ids)
    {
        return cusIotVoltageHarmonicMapper.deleteCusIotVoltageHarmonicByIds(ids);
    }

    /**
     * 删除谐波电压数据信息
     * 
     * @param id 谐波电压数据主键
     * @return 结果
     */
    @Override
    public int deleteCusIotVoltageHarmonicById(Long id)
    {
        return cusIotVoltageHarmonicMapper.deleteCusIotVoltageHarmonicById(id);
    }
}
