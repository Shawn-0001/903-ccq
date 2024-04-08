package com.ruoyi.iot.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备列表对象 cus_iot_device_list
 *
 * @author Shawn
 * @date 2024-04-01
 */
public class CusIotDeviceList extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 自增序列
     */
    private Long id;

    /**
     * 每条数据的UUID
     */
    @Excel(name = "每条数据的UUID")
    private String latestUUID;

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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setLatestUUID(String latestUUID) {
        this.latestUUID = latestUUID;
    }

    public String getLatestUUID() {
        return latestUUID;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("latestUUID", getLatestUUID())
                .append("timestamp", getTimestamp())
                .append("deviceId", getDeviceId())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
