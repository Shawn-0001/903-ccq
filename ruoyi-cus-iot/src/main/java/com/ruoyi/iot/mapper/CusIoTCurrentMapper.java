package com.ruoyi.iot.mapper;

import java.util.List;

import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.iot.domain.CusIoTCurrent;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 电流数据Mapper接口
 *
 * @author Shawn
 * @date 2024-03-25
 */
public interface CusIoTCurrentMapper
{
    /**
     * 查询电流数据
     *
     * @param id 电流数据主键
     * @return 电流数据
     */
    public CusIoTCurrent selectCusIoTCurrentById(Long id);

    /**
     * 查询最新一条电流数据
     * @param deviceId 设备编码
     * @return 电流数据
     */
    public CusIoTCurrent selectCusIoTCurrentLatestOne(String deviceId);

    /**
     * 精确获取电流数据详细信息
     * @param cusIoTCurrent 电流数据
     * @return 电流数据集合
     */
    public List<CusIoTCurrent> selectCusIoTCurrentByField(CusIoTCurrent cusIoTCurrent);

    /**
     * 查询电流数据列表
     *
     * @param cusIoTCurrent 电流数据
     * @return 电流数据集合
     */
    public List<CusIoTCurrent> selectCusIoTCurrentList(CusIoTCurrent cusIoTCurrent);

    /**
     * 新增电流数据
     *
     * @param cusIoTCurrent 电流数据
     * @return 结果
     */
    public int insertCusIoTCurrent(CusIoTCurrent cusIoTCurrent);

    /**
     * 修改电流数据
     *
     * @param cusIoTCurrent 电流数据
     * @return 结果
     */
    public int updateCusIoTCurrent(CusIoTCurrent cusIoTCurrent);

    /**
     * 删除电流数据
     *
     * @param id 电流数据主键
     * @return 结果
     */
    public int deleteCusIoTCurrentById(Long id);

    /**
     * 批量删除电流数据
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCusIoTCurrentByIds(Long[] ids);
}
