package com.ruoyi.iot.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 功率数据对象 cus_iot_power_data
 *
 * @author Shawn
 * @date 2024-03-26
 */
public class CusIotPowerData extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 自增序列
     */
    private Long id;

    /**
     * 上传的时间戳（二进制解析成10进制）
     */
    @Excel(name = "上传的时间戳", readConverterExp = "二=进制解析成10进制")
    private Long timestamp;

    /**
     * 设备ID
     */
    @Excel(name = "设备ID")
    private String deviceId;

    /**
     * A:active_power:有功功率
     * R:reactive_power:无功功率
     * F:power_factor:功率因素
     */
    @Excel(name = "A:active_power:有功功率; R:reactive_power:无功功率; F:power_factor:功率因素")
    private String type;

    /**
     * A相
     */
    @Excel(name = "A相")
    private String phaseA;

    /**
     * B相
     */
    @Excel(name = "B相")
    private String phaseB;

    /**
     * C相
     */
    @Excel(name = "C相")
    private String phaseC;

    /**
     * 总计
     */
    @Excel(name = "总计")
    private String total;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setPhaseA(String phaseA) {
        this.phaseA = phaseA;
    }

    public String getPhaseA() {
        return phaseA;
    }

    public void setPhaseB(String phaseB) {
        this.phaseB = phaseB;
    }

    public String getPhaseB() {
        return phaseB;
    }

    public void setPhaseC(String phaseC) {
        this.phaseC = phaseC;
    }

    public String getPhaseC() {
        return phaseC;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("timestamp", getTimestamp())
                .append("deviceId", getDeviceId())
                .append("type", getType())
                .append("phaseA", getPhaseA())
                .append("phaseB", getPhaseB())
                .append("phaseC", getPhaseC())
                .append("total", getTotal())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
