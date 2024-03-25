package com.ruoyi.thread;

import com.ruoyi.netty.NettyServer;
import com.ruoyi.netty.NettyServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
public class NettyExecutor {
    /**
     * netty服务端
     */
    public static NettyServer nettyServer;

    private static final Logger logger = LoggerFactory.getLogger(NettyExecutor.class);

    @Autowired
    private void setNettyServer(NettyServer nettyServer) {
        this.nettyServer = nettyServer;
    }

    @Async("threadPoolTaskExecutor")
    public static void nettyServerRun() {
        logger.debug("Starting netty server!");
        nettyServer.serverRun();
    }
}
