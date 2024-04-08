package com.ruoyi.iot.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.iot.mapper.CusIotCurrentHarmonicMapper;
import com.ruoyi.iot.domain.CusIotCurrentHarmonic;
import com.ruoyi.iot.service.ICusIotCurrentHarmonicService;

/**
 * 谐波电流数据Service业务层处理
 *
 * @author Shawn
 * @date 2024-03-26
 */
@Service
public class CusIotCurrentHarmonicServiceImpl implements ICusIotCurrentHarmonicService {
    @Autowired
    private CusIotCurrentHarmonicMapper cusIotCurrentHarmonicMapper;

    /**
     * 查询谐波电流数据
     *
     * @param id 谐波电流数据主键
     * @return 谐波电流数据
     */
    @Override
    public CusIotCurrentHarmonic selectCusIotCurrentHarmonicById(Long id) {
        return cusIotCurrentHarmonicMapper.selectCusIotCurrentHarmonicById(id);
    }

    /**
     * 查询谐波电流数据列表
     *
     * @param cusIotCurrentHarmonic 谐波电流数据
     * @return 谐波电流数据
     */
    @Override
    public List<CusIotCurrentHarmonic> selectCusIotCurrentHarmonicList(CusIotCurrentHarmonic cusIotCurrentHarmonic) {
        return cusIotCurrentHarmonicMapper.selectCusIotCurrentHarmonicList(cusIotCurrentHarmonic);
    }

    /**
     * 查询最新一条谐波电流数据详细信息
     *
     * @param deviceId 谐波电流数据主键
     * @return 谐波电流数据
     */
    public CusIotCurrentHarmonic selectCusIoTCurrentHarmonicLatestOne(String deviceId) {
        return cusIotCurrentHarmonicMapper.selectCusIoTCurrentHarmonicLatestOne(deviceId);
    }

    /**
     * 新增谐波电流数据
     *
     * @param cusIotCurrentHarmonic 谐波电流数据
     * @return 结果
     */
    @Override
    public int insertCusIotCurrentHarmonic(CusIotCurrentHarmonic cusIotCurrentHarmonic) {
        return cusIotCurrentHarmonicMapper.insertCusIotCurrentHarmonic(cusIotCurrentHarmonic);
    }

    /**
     * 修改谐波电流数据
     *
     * @param cusIotCurrentHarmonic 谐波电流数据
     * @return 结果
     */
    @Override
    public int updateCusIotCurrentHarmonic(CusIotCurrentHarmonic cusIotCurrentHarmonic) {
        return cusIotCurrentHarmonicMapper.updateCusIotCurrentHarmonic(cusIotCurrentHarmonic);
    }

    /**
     * 批量删除谐波电流数据
     *
     * @param ids 需要删除的谐波电流数据主键
     * @return 结果
     */
    @Override
    public int deleteCusIotCurrentHarmonicByIds(Long[] ids) {
        return cusIotCurrentHarmonicMapper.deleteCusIotCurrentHarmonicByIds(ids);
    }

    /**
     * 删除谐波电流数据信息
     *
     * @param id 谐波电流数据主键
     * @return 结果
     */
    @Override
    public int deleteCusIotCurrentHarmonicById(Long id) {
        return cusIotCurrentHarmonicMapper.deleteCusIotCurrentHarmonicById(id);
    }
}
