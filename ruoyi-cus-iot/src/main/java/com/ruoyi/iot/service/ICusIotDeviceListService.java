package com.ruoyi.iot.service;

import java.util.List;
import com.ruoyi.iot.domain.CusIotDeviceList;

/**
 * 设备列表Service接口
 *
 * @author Shawn
 * @date 2024-04-01
 */
public interface ICusIotDeviceListService
{
    /**
     * 查询设备列表
     *
     * @param id 设备列表主键
     * @return 设备列表
     */
    public CusIotDeviceList selectCusIotDeviceListById(Long id);

    /**
     * 查询设备列表列表
     *
     * @param cusIotDeviceList 设备列表
     * @return 设备列表集合
     */
    public List<CusIotDeviceList> selectCusIotDeviceListList(CusIotDeviceList cusIotDeviceList);

    /**
     * 精确设备列表
     * @param cusIotDeviceList 设备列表
     * @return 设备列表集合
     */
    public List<CusIotDeviceList> selectCusIoTDeviceByField(CusIotDeviceList cusIotDeviceList);

    /**
     * 新增设备列表
     *
     * @param cusIotDeviceList 设备列表
     * @return 结果
     */
    public int insertCusIotDeviceList(CusIotDeviceList cusIotDeviceList);

    /**
     * 修改设备列表
     *
     * @param cusIotDeviceList 设备列表
     * @return 结果
     */
    public int updateCusIotDeviceList(CusIotDeviceList cusIotDeviceList);

    /**
     * 批量删除设备列表
     *
     * @param ids 需要删除的设备列表主键集合
     * @return 结果
     */
    public int deleteCusIotDeviceListByIds(Long[] ids);

    /**
     * 删除设备列表信息
     *
     * @param id 设备列表主键
     * @return 结果
     */
    public int deleteCusIotDeviceListById(Long id);
}
