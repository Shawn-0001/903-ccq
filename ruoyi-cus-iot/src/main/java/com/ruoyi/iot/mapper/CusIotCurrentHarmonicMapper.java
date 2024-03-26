package com.ruoyi.iot.mapper;

import java.util.List;
import com.ruoyi.iot.domain.CusIotCurrentHarmonic;

/**
 * 谐波电流数据Mapper接口
 * 
 * @author Shawn
 * @date 2024-03-26
 */
public interface CusIotCurrentHarmonicMapper 
{
    /**
     * 查询谐波电流数据
     * 
     * @param id 谐波电流数据主键
     * @return 谐波电流数据
     */
    public CusIotCurrentHarmonic selectCusIotCurrentHarmonicById(Long id);

    /**
     * 查询谐波电流数据列表
     * 
     * @param cusIotCurrentHarmonic 谐波电流数据
     * @return 谐波电流数据集合
     */
    public List<CusIotCurrentHarmonic> selectCusIotCurrentHarmonicList(CusIotCurrentHarmonic cusIotCurrentHarmonic);

    /**
     * 新增谐波电流数据
     * 
     * @param cusIotCurrentHarmonic 谐波电流数据
     * @return 结果
     */
    public int insertCusIotCurrentHarmonic(CusIotCurrentHarmonic cusIotCurrentHarmonic);

    /**
     * 修改谐波电流数据
     * 
     * @param cusIotCurrentHarmonic 谐波电流数据
     * @return 结果
     */
    public int updateCusIotCurrentHarmonic(CusIotCurrentHarmonic cusIotCurrentHarmonic);

    /**
     * 删除谐波电流数据
     * 
     * @param id 谐波电流数据主键
     * @return 结果
     */
    public int deleteCusIotCurrentHarmonicById(Long id);

    /**
     * 批量删除谐波电流数据
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCusIotCurrentHarmonicByIds(Long[] ids);
}
