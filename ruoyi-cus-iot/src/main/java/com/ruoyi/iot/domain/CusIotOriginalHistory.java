package com.ruoyi.iot.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 原始数据历史对象 cus_iot_original_history
 *
 * @author Shawn
 * @date 2024-04-11
 */
public class CusIotOriginalHistory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 自增序列 */
    private Long id;

    /** 每条数据的UUID */
    @Excel(name = "每条数据的UUID")
    private String UUID;

    /** 上传的时间戳（二进制解析成10进制） */
    @Excel(name = "上传的时间戳", readConverterExp = "二=进制解析成10进制")
    private Long timestamp;

    /** 设备ID */
    @Excel(name = "设备ID")
    private String deviceId;

    /** 原始数据 */
    @Excel(name = "原始数据")
    private String data;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setUUID(String UUID)
    {
        this.UUID = UUID;
    }

    public String getUUID()
    {
        return UUID;
    }
    public void setTimestamp(Long timestamp)
    {
        this.timestamp = timestamp;
    }

    public Long getTimestamp()
    {
        return timestamp;
    }
    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }

    public String getDeviceId()
    {
        return deviceId;
    }
    public void setData(String data)
    {
        this.data = data;
    }

    public String getData()
    {
        return data;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("UUID", getUUID())
                .append("timestamp", getTimestamp())
                .append("deviceId", getDeviceId())
                .append("data", getData())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
