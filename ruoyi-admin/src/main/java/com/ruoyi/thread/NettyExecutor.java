package com.ruoyi.thread;

import com.ruoyi.netty.NettyServer;
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
//    private static ThreadServer threadServer;

//    @Autowired
//    private void setThreadServer(ThreadServer threadServer) {
//        this.threadServer = threadServer;
//
//    }

    @Autowired
    private void setNettyServer(NettyServer nettyServer) {
        this.nettyServer = nettyServer;
    }

    @Async("threadPoolTaskExecutor")
    public static void nettyServerRun() {
        nettyServer.serverRun();
        System.out.println("Starting netty server!");
    }

}
