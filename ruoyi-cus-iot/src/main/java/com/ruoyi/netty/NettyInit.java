package com.ruoyi.netty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "netty-init")
public class NettyInit {
    /**
     * Netty可进行多端口监听 这里初步只做单端口监听
     */
    private List<Integer> serverPortList;

    /**
     * 服务端监听端口
     */
    private Integer serverPort;
    /**
     * 设置解码器最大长度 防止恶意数据
     */
    private Integer deCodeSize;
    /**
     * 分隔符号
     */
    private String separator;


    public List<Integer> getServerPortList() {
        return serverPortList;
    }

    public void setServerPortList(List<Integer> serverPortList) {
        this.serverPortList = serverPortList;
    }

    public Integer getDeCodeSize() {
        return deCodeSize;
    }

    public void setDeCodeSize(Integer deCodeSize) {
        this.deCodeSize = deCodeSize;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }
}
