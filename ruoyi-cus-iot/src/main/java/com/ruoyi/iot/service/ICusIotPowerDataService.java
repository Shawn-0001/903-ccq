package com.ruoyi.iot.service;

import java.util.List;
import com.ruoyi.iot.domain.CusIotPowerData;

/**
 * 功率数据Service接口
 * 
 * @author Shawn
 * @date 2024-03-26
 */
public interface ICusIotPowerDataService 
{
    /**
     * 查询功率数据
     * 
     * @param id 功率数据主键
     * @return 功率数据
     */
    public CusIotPowerData selectCusIotPowerDataById(Long id);

    /**
     * 查询功率数据列表
     * 
     * @param cusIotPowerData 功率数据
     * @return 功率数据集合
     */
    public List<CusIotPowerData> selectCusIotPowerDataList(CusIotPowerData cusIotPowerData);

    /**
     *  精确查询功率数据列表
     *
     * @param cusIotPowerData 功率数据
     * @return 功率数据集合
     */
    public List<CusIotPowerData> selectCusIotPowerDataByField(CusIotPowerData cusIotPowerData);

    /**
     * 新增功率数据
     * 
     * @param cusIotPowerData 功率数据
     * @return 结果
     */
    public int insertCusIotPowerData(CusIotPowerData cusIotPowerData);

    /**
     * 修改功率数据
     * 
     * @param cusIotPowerData 功率数据
     * @return 结果
     */
    public int updateCusIotPowerData(CusIotPowerData cusIotPowerData);

    /**
     * 批量删除功率数据
     * 
     * @param ids 需要删除的功率数据主键集合
     * @return 结果
     */
    public int deleteCusIotPowerDataByIds(Long[] ids);

    /**
     * 删除功率数据信息
     * 
     * @param id 功率数据主键
     * @return 结果
     */
    public int deleteCusIotPowerDataById(Long id);
}
