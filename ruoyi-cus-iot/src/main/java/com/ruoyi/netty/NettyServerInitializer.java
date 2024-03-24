package com.ruoyi.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Autowired
    private NettyServerHandler nettyServerHandler;

    @Autowired
    private NettyInit nettyInit;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();

        // 分隔符解码器
//        ByteBuf delimiter = Unpooled.copiedBuffer(nettyInit.getSeparator().getBytes());
//        channelPipeline .addLast(new DelimiterBasedFrameDecoder(nettyInit.getDeCodeSize(), delimiter));
        channelPipeline .addLast(new FixedLengthFrameDecoder(nettyInit.getDeCodeSize()));
        // 字符串解码 和 编码
        //解码过程
        channelPipeline.addLast(new MyDecoder());
        // 处理器
        channelPipeline.addLast(nettyServerHandler);
    }
}
