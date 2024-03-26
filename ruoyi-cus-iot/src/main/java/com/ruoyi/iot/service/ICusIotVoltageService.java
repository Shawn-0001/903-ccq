package com.ruoyi.iot.service;

import java.util.List;
import com.ruoyi.iot.domain.CusIotVoltage;

/**
 * 电压数据Service接口
 *
 * @author Shawn
 * @date 2024-03-26
 */
public interface ICusIotVoltageService
{
    /**
     * 查询电压数据
     *
     * @param id 电压数据主键
     * @return 电压数据
     */
    public CusIotVoltage selectCusIotVoltageById(Long id);

    /**
     * 查询电压数据列表
     *
     * @param cusIotVoltage 电压数据
     * @return 电压数据集合
     */
    public List<CusIotVoltage> selectCusIotVoltageList(CusIotVoltage cusIotVoltage);

    /**
     * 新增电压数据
     *
     * @param cusIotVoltage 电压数据
     * @return 结果
     */
    public int insertCusIotVoltage(CusIotVoltage cusIotVoltage);

    /**
     * 修改电压数据
     *
     * @param cusIotVoltage 电压数据
     * @return 结果
     */
    public int updateCusIotVoltage(CusIotVoltage cusIotVoltage);

    /**
     * 批量删除电压数据
     *
     * @param ids 需要删除的电压数据主键集合
     * @return 结果
     */
    public int deleteCusIotVoltageByIds(Long[] ids);

    /**
     * 删除电压数据信息
     *
     * @param id 电压数据主键
     * @return 结果
     */
    public int deleteCusIotVoltageById(Long id);
}
