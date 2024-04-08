package com.ruoyi.iot.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.iot.mapper.CusIotDeviceListMapper;
import com.ruoyi.iot.domain.CusIotDeviceList;
import com.ruoyi.iot.service.ICusIotDeviceListService;

/**
 * 设备列表Service业务层处理
 *
 * @author Shawn
 * @date 2024-04-01
 */
@Service
public class CusIotDeviceListServiceImpl implements ICusIotDeviceListService
{
    @Autowired
    private CusIotDeviceListMapper cusIotDeviceListMapper;

    /**
     * 查询设备列表
     *
     * @param id 设备列表主键
     * @return 设备列表
     */
    @Override
    public CusIotDeviceList selectCusIotDeviceListById(Long id)
    {
        return cusIotDeviceListMapper.selectCusIotDeviceListById(id);
    }

    /**
     * 查询设备列表列表
     *
     * @param cusIotDeviceList 设备列表
     * @return 设备列表
     */
    @Override
    public List<CusIotDeviceList> selectCusIotDeviceListList(CusIotDeviceList cusIotDeviceList)
    {
        return cusIotDeviceListMapper.selectCusIotDeviceListList(cusIotDeviceList);
    }

    /**
     * 精确设备列表
     * @param cusIotDeviceList 设备列表
     * @return 设备列表集合
     */
    public List<CusIotDeviceList> selectCusIoTDeviceByField(CusIotDeviceList cusIotDeviceList){
        return cusIotDeviceListMapper.selectCusIoTDeviceByField(cusIotDeviceList);
    }

    /**
     * 新增设备列表
     *
     * @param cusIotDeviceList 设备列表
     * @return 结果
     */
    @Override
    public int insertCusIotDeviceList(CusIotDeviceList cusIotDeviceList)
    {
        return cusIotDeviceListMapper.insertCusIotDeviceList(cusIotDeviceList);
    }

    /**
     * 修改设备列表
     *
     * @param cusIotDeviceList 设备列表
     * @return 结果
     */
    @Override
    public int updateCusIotDeviceList(CusIotDeviceList cusIotDeviceList)
    {
        return cusIotDeviceListMapper.updateCusIotDeviceList(cusIotDeviceList);
    }

    /**
     * 批量删除设备列表
     *
     * @param ids 需要删除的设备列表主键
     * @return 结果
     */
    @Override
    public int deleteCusIotDeviceListByIds(Long[] ids)
    {
        return cusIotDeviceListMapper.deleteCusIotDeviceListByIds(ids);
    }

    /**
     * 删除设备列表信息
     *
     * @param id 设备列表主键
     * @return 结果
     */
    @Override
    public int deleteCusIotDeviceListById(Long id)
    {
        return cusIotDeviceListMapper.deleteCusIotDeviceListById(id);
    }
}
