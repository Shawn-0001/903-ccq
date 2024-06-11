package com.ruoyi.iot.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 电压数据对象 cus_iot_voltage
 *
 * @author Shawn
 * @date 2024-03-26
 */
public class CusIotVoltage extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 自增序列
     */
    private Long id;

    /**
     * 每条数据的UUID
     */
    @Excel(name = "每条数据的UUID")
    private String UUID;

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
     * 电压A，周期1
     */
    @Excel(name = "电压A，周期1")
    private String voltageA1;

    /** 电压A，周期1傅里叶转换后结果 */
    @Excel(name = "电压A，周期1傅里叶转换后结果")
    private String voltageAFFT;

    /**
     * 电压A，周期2
     */
    @Excel(name = "电压A，周期2")
    private String voltageA2;

    /**
     * 电压B，周期1
     */
    @Excel(name = "电压B，周期1")
    private String voltageB1;

    /** 电压B，周期1傅里叶转换后结果 */
    @Excel(name = "电压B，周期1傅里叶转换后结果")
    private String voltageBFFT;

    /**
     * 电压B，周期2
     */
    @Excel(name = "电压B，周期2")
    private String voltageB2;

    /**
     * 电压C，周期1
     */
    @Excel(name = "电压C，周期1")
    private String voltageC1;

    /** 电压C，周期1傅里叶转换后结果 */
    @Excel(name = "电压C，周期1傅里叶转换后结果")
    private String voltageCFFT;

    /**
     * 电压C，周期2
     */
    @Excel(name = "电压C，周期2")
    private String voltageC2;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getUUID() {
        return UUID;
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

    public void setVoltageA1(String voltageA1) {
        this.voltageA1 = voltageA1;
    }

    public String getVoltageAFFT() {
        return voltageAFFT;
    }

    public void setVoltageAFFT(String voltageAFFT) {
        this.voltageAFFT = voltageAFFT;
    }

    public String getVoltageA1() {
        return voltageA1;
    }

    public void setVoltageA2(String voltageA2) {
        this.voltageA2 = voltageA2;
    }

    public String getVoltageA2() {
        return voltageA2;
    }

    public void setVoltageB1(String voltageB1) {
        this.voltageB1 = voltageB1;
    }

    public String getVoltageB1() {
        return voltageB1;
    }

    public void setVoltageBFFT(String voltageBFFT) {
        this.voltageBFFT = voltageBFFT;
    }

    public String getVoltageBFFT() {
        return voltageBFFT;
    }

    public void setVoltageB2(String voltageB2) {
        this.voltageB2 = voltageB2;
    }

    public String getVoltageB2() {
        return voltageB2;
    }

    public void setVoltageC1(String voltageC1) {
        this.voltageC1 = voltageC1;
    }

    public String getVoltageC1() {
        return voltageC1;
    }

    public String getVoltageCFFT() {
        return voltageCFFT;
    }

    public void setVoltageCFFT(String voltageCFFT) {
        this.voltageCFFT = voltageCFFT;
    }

    public void setVoltageC2(String voltageC2) {
        this.voltageC2 = voltageC2;
    }

    public String getVoltageC2() {
        return voltageC2;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("UUID", getUUID())
                .append("timestamp", getTimestamp())
                .append("deviceId", getDeviceId())
                .append("voltageA1", getVoltageA1())
                .append("voltageA2", getVoltageA2())
                .append("voltageAFFT", getVoltageAFFT())
                .append("voltageB1", getVoltageB1())
                .append("voltageB2", getVoltageB2())
                .append("voltageBFFT", getVoltageBFFT())
                .append("voltageC1", getVoltageC1())
                .append("voltageC2", getVoltageC2())
                .append("voltageCFFT", getVoltageCFFT())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
