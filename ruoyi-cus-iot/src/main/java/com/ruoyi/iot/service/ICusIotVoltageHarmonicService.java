package com.ruoyi.iot.service;

import java.util.List;
import com.ruoyi.iot.domain.CusIotVoltageHarmonic;

/**
 * 谐波电压数据Service接口
 * 
 * @author Shawn
 * @date 2024-03-26
 */
public interface ICusIotVoltageHarmonicService 
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
     * 批量删除谐波电压数据
     * 
     * @param ids 需要删除的谐波电压数据主键集合
     * @return 结果
     */
    public int deleteCusIotVoltageHarmonicByIds(Long[] ids);

    /**
     * 删除谐波电压数据信息
     * 
     * @param id 谐波电压数据主键
     * @return 结果
     */
    public int deleteCusIotVoltageHarmonicById(Long id);
}
