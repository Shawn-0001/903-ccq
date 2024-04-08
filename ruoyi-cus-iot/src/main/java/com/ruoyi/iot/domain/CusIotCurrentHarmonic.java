package com.ruoyi.iot.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 谐波电流数据对象 cus_iot_current_harmonic
 *
 * @author Shawn
 * @date 2024-03-26
 */
public class CusIotCurrentHarmonic extends BaseEntity {
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
     * A相谐波电流
     */
    @Excel(name = "A相谐波电流")
    private String harmonicA;

    /**
     * B相谐波电流
     */
    @Excel(name = "B相谐波电流")
    private String harmonicB;

    /**
     * C相谐波电流
     */
    @Excel(name = "C相谐波电流")
    private String harmonicC;

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

    public void setHarmonicA(String harmonicA) {
        this.harmonicA = harmonicA;
    }

    public String getHarmonicA() {
        return harmonicA;
    }

    public void setHarmonicB(String harmonicB) {
        this.harmonicB = harmonicB;
    }

    public String getHarmonicB() {
        return harmonicB;
    }

    public void setHarmonicC(String harmonicC) {
        this.harmonicC = harmonicC;
    }

    public String getHarmonicC() {
        return harmonicC;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("UUID", getUUID())
                .append("timestamp", getTimestamp())
                .append("deviceId", getDeviceId())
                .append("harmonicA", getHarmonicA())
                .append("harmonicB", getHarmonicB())
                .append("harmonicC", getHarmonicC())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
