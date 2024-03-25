package com.ruoyi.iot.service.impl;

import java.util.List;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.iot.mapper.CusIoTCurrentMapper;
import com.ruoyi.iot.domain.CusIoTCurrent;
import com.ruoyi.iot.service.ICusIoTCurrentService;

/**
 * 电流数据Service业务层处理
 *
 * @author Shawn
 * @date 2024-03-25
 */
@Service
public class CusIoTCurrentServiceImpl implements ICusIoTCurrentService
{
    @Autowired
    private CusIoTCurrentMapper cusIoTCurrentMapper;

    /**
     * 查询电流数据
     *
     * @param id 电流数据主键
     * @return 电流数据
     */
    @Override
    public CusIoTCurrent selectCusIoTCurrentById(Long id)
    {
        return cusIoTCurrentMapper.selectCusIoTCurrentById(id);
    }

    /**
     * 查询电流数据列表
     *
     * @param cusIoTCurrent 电流数据
     * @return 电流数据
     */
    @Override
    public List<CusIoTCurrent> selectCusIoTCurrentList(CusIoTCurrent cusIoTCurrent)
    {
        return cusIoTCurrentMapper.selectCusIoTCurrentList(cusIoTCurrent);
    }

    /**
     * 新增电流数据
     *
     * @param cusIoTCurrent 电流数据
     * @return 结果
     */
    @Override
    public int insertCusIoTCurrent(CusIoTCurrent cusIoTCurrent)
    {
        return cusIoTCurrentMapper.insertCusIoTCurrent(cusIoTCurrent);
    }

    /**
     * 修改电流数据
     *
     * @param cusIoTCurrent 电流数据
     * @return 结果
     */
    @Override
    public int updateCusIoTCurrent(CusIoTCurrent cusIoTCurrent)
    {
        return cusIoTCurrentMapper.updateCusIoTCurrent(cusIoTCurrent);
    }

    /**
     * 批量删除电流数据
     *
     * @param ids 需要删除的电流数据主键
     * @return 结果
     */
    @Override
    public int deleteCusIoTCurrentByIds(Long[] ids)
    {
        return cusIoTCurrentMapper.deleteCusIoTCurrentByIds(ids);
    }

    /**
     * 删除电流数据信息
     *
     * @param id 电流数据主键
     * @return 结果
     */
    @Override
    public int deleteCusIoTCurrentById(Long id)
    {
        return cusIoTCurrentMapper.deleteCusIoTCurrentById(id);
    }
}
