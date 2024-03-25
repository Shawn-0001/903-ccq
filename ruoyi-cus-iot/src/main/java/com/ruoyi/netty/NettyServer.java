package com.ruoyi.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NettyServer {
    @Autowired
    private NettyServerInitializer nettyServerInitializer;

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    @Autowired
    private NettyInit nettys;

    /**
     * 启动服务
     */
    public void serverRun() {
      /*  if (!isRun) {
            return;
        }*/
        // 监听线程组 监听客户请求
        EventLoopGroup acceptorGroup = new NioEventLoopGroup();
        // 处理客户端相关操作线程组 ,负责处理与客户端的数据通讯
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //设置 监听组 线程组 初始化器
            serverBootstrap.group(acceptorGroup, clientGroup)
                    .channel(NioServerSocketChannel.class)
                    // 点烟器初始化器
                    .childHandler(nettyServerInitializer);
            // 绑定端口号
            ChannelFuture channelFuture = serverBootstrap.bind(nettys.getServerPort()).sync();
            // 可绑定多端口监听 当前版本只简单处理 只绑定一个端口

            logger.debug("Netty服务已经启动,端口:" + nettys.getServerPort());
            logger.debug("local ip" + channelFuture.channel().localAddress());
            // 等待客户端关闭连接
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            acceptorGroup.shutdownGracefully();
            clientGroup.shutdownGracefully();
        }
    }
}
