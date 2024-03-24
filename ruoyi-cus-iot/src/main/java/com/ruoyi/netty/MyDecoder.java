package com.ruoyi.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {

        byte[] bytes = new byte[byteBuf.readableBytes()];
        //复制内容到字节数组b
        byteBuf.readBytes(bytes);
        list.add(bytes);
    }
}
