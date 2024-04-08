package com.ruoyi.iot.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 电流数据对象 cus_iot_current
 *
 * @author Shawn
 * @date 2024-03-25
 */
public class CusIoTCurrent extends BaseEntity
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

    /** 电流A，周期1 */
    @Excel(name = "电流A，周期1")
    private String currentA1;

    /** 电流A，周期 */
    @Excel(name = "电流A，周期")
    private String currentA2;

    /** 电流A，周期1傅里叶转换后结果 */
    @Excel(name = "电流A，周期1傅里叶转换后结果")
    private String currentAFFT;

    /** 电流B，周期1 */
    @Excel(name = "电流B，周期1")
    private String currentB1;

    /** 电流B，周期 */
    @Excel(name = "电流B，周期")
    private String currentB2;

    /** 电流B，周期1傅里叶转换后结果 */
    @Excel(name = "电流B，周期1傅里叶转换后结果")
    private String currentBFFT;

    /** 电流C，周期1 */
    @Excel(name = "电流C，周期1")
    private String currentC1;

    /** 电流C，周期 */
    @Excel(name = "电流C，周期")
    private String currentC2;

    /** 电流C，周期1傅里叶转换后结果 */
    @Excel(name = "电流C，周期1傅里叶转换后结果")
    private String currentCFFT;

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
    public void setCurrentA1(String currentA1)
    {
        this.currentA1 = currentA1;
    }

    public String getCurrentA1()
    {
        return currentA1;
    }
    public void setCurrentA2(String currentA2)
    {
        this.currentA2 = currentA2;
    }

    public String getCurrentA2()
    {
        return currentA2;
    }
    public void setCurrentAFFT(String currentAFFT)
    {
        this.currentAFFT = currentAFFT;
    }

    public String getCurrentAFFT()
    {
        return currentAFFT;
    }
    public void setCurrentB1(String currentB1)
    {
        this.currentB1 = currentB1;
    }

    public String getCurrentB1()
    {
        return currentB1;
    }
    public void setCurrentB2(String currentB2)
    {
        this.currentB2 = currentB2;
    }

    public String getCurrentB2()
    {
        return currentB2;
    }
    public void setCurrentBFFT(String currentBFFT)
    {
        this.currentBFFT = currentBFFT;
    }

    public String getCurrentBFFT()
    {
        return currentBFFT;
    }
    public void setCurrentC1(String currentC1)
    {
        this.currentC1 = currentC1;
    }

    public String getCurrentC1()
    {
        return currentC1;
    }
    public void setCurrentC2(String currentC2)
    {
        this.currentC2 = currentC2;
    }

    public String getCurrentC2()
    {
        return currentC2;
    }
    public void setCurrentCFFT(String currentCFFT)
    {
        this.currentCFFT = currentCFFT;
    }

    public String getCurrentCFFT()
    {
        return currentCFFT;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("UUID", getUUID())
                .append("timestamp", getTimestamp())
                .append("deviceId", getDeviceId())
                .append("currentA1", getCurrentA1())
                .append("currentA2", getCurrentA2())
                .append("currentAFFT", getCurrentAFFT())
                .append("currentB1", getCurrentB1())
                .append("currentB2", getCurrentB2())
                .append("currentBFFT", getCurrentBFFT())
                .append("currentC1", getCurrentC1())
                .append("currentC2", getCurrentC2())
                .append("currentCFFT", getCurrentCFFT())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}