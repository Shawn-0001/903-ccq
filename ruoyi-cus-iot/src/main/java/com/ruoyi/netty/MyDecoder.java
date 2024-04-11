package com.ruoyi.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MyDecoder extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(MyDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 判断接收的字节长度， 小于3912 继续接收， 如果等于3912开始解析， 如果大于3912， 则清空数据重新接收。
        switch (Integer.compare(byteBuf.readableBytes(), 3912)) {
            // 小于3912
            case -1:
                logger.debug("数据长度不足【3912】个字节， 实际长度：【" + byteBuf.readableBytes() + "】个字节， 等待下次传输。");
                break;
            // 大于3912
            case 1:
                logger.debug("数据长度超过期望值， 期望【3912】个字节， 实际长度：【" + byteBuf.readableBytes() + "】个字节， 丢弃并清空已接收数据， 等待下次传输。");
                byteBuf.clear();
                break;
            // 等于3912
            case 0:
                // 复制内容到字节数组b，
                byte[] bytes = new byte[byteBuf.readableBytes()];
                byteBuf.readBytes(bytes);
                list.add(bytes);

//                // header  2字节 不使用 16进制校验值为： 68 3F
//                // 在全部字节数组中， 截取头部2字节
//                byte[] segment_1 = subBytes(bytes, 0, 2);
//                // 解析成16进制， 判断是否合规。
//                String str1 = bytesToHexString(segment_1);
//                if (!(StringUtils.equals(str1, "683f") || StringUtils.equals(str1, "683F"))) {
//                    logger.debug("第【1】段数据解析错误， 期望的数据头为 683F 或 683f， 得到的文件头为: " + str1);
//                    // 缓冲区的释放
//                    byteBuf.clear();
//                } else {
//                    list.add(bytes);
//                }
                break;
        }
    }
}
