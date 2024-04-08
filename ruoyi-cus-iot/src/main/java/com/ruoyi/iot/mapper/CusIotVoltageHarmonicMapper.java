package com.ruoyi.iot.mapper;

import java.util.List;

import com.ruoyi.iot.domain.CusIotCurrentHarmonic;
import com.ruoyi.iot.domain.CusIotVoltageHarmonic;

/**
 * 谐波电压数据Mapper接口
 * 
 * @author Shawn
 * @date 2024-03-26
 */
public interface CusIotVoltageHarmonicMapper 
{
    /**
     * 查询谐波电压数据
     * 
     * @param id 谐波电压数据主键
     * @return 谐波电压数据
     */
    public CusIotVoltageHarmonic selectCusIotVoltageHarmonicById(Long id);

    /**
     * 查询谐波电压数据列表
     * 
     * @param cusIotVoltageHarmonic 谐波电压数据
     * @return 谐波电压数据集合
     */
    public List<CusIotVoltageHarmonic> selectCusIotVoltageHarmonicList(CusIotVoltageHarmonic cusIotVoltageHarmonic);

    /**
     * 查询最新一条谐波电压数据详细信息
     *
     * @param deviceId 谐波电压数据主键
     * @return 谐波电压数据
     */
    public CusIotVoltageHarmonic selectCusIoTVoltageHarmonicLatestOne(String deviceId);

    /**
     * 新增谐波电压数据
     * 
     * @param cusIotVoltageHarmonic 谐波电压数据
     * @return 结果
     */
    public int insertCusIotVoltageHarmonic(CusIotVoltageHarmonic cusIotVoltageHarmonic);

    /**
     * 修改谐波电压数据
     * 
     * @param cusIotVoltageHarmonic 谐波电压数据
     * @return 结果
     */
    public int updateCusIotVoltageHarmonic(CusIotVoltageHarmonic cusIotVoltageHarmonic);

    /**
     * 删除谐波电压数据
     * 
     * @param id 谐波电压数据主键
     * @return 结果
     */
    public int deleteCusIotVoltageHarmonicById(Long id);

    /**
     * 批量删除谐波电压数据
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCusIotVoltageHarmonicByIds(Long[] ids);
}
