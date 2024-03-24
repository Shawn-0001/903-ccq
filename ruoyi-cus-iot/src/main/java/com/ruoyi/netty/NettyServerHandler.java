package com.ruoyi.netty;

import com.ruoyi.common.utils.StringUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.jtransforms.fft.DoubleFFT_1D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

@Component
@ChannelHandler.Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            byte[] bytes = (byte[]) msg;
//            String str = bytesToHexString(bytes);
//            logger.debug("解包后的数据（16进制数据）: " + str);
//            logger.debug("解包后的数据长度（16进制数据）: " + str.length());
            logger.info("解包后的数据长度（字节数据）: " + bytes.length);

            // 开始解析数据
            // begin byte数字截取起始位置
            int begin = 0;
            // count 截取的byte长度
            int count = 0;

            // header
            // 2字节 count = 2; 不使用 68 3F
            count = 2;
            // 在全部字节数组中， 截取头部2字节
            byte[] segment_1 = subBytes(bytes, begin, count);
            // 解析成16进制， 判断是否合规。
            String str1 = bytesToHexString(segment_1);
            if (!(StringUtils.equals(str1, "683f") || StringUtils.equals(str1, "683F"))) {
                logger.debug("第【1】段数据解析错误， 期望的数据头为 683F 或 683f， 得到的文件头为: " + str1);
                return;
            }
            // 计算下一个段开始的位置
            begin = begin + count;
            logger.debug("第【1】段数据头校验通过，开始解析数据-->");

            // timestamp
            // 4字节，count = 4; 不使用， 上传的时间戳（二进制解析成10进制， 数值应该是错误的， 或者硬件时间没有调整）
            count = 4;
            // 在字节数组中截取该段的字节
            byte[] segment_2 = subBytes(bytes, begin, count);
            // 字节转int
            // TODO
            int timestamp = bytesToInt(segment_2);
            // 计算下一个段开始的位置
            begin = begin + count;

            // 设备ID
            // 16 字节， 实际有2字节是 0； count = 16; 转ASCII， 文本
            count = 16;
            // 在字节数组中截取该段的字节
            byte[] segment_3 = subBytes(bytes, begin, count);
            // 字节转字符串， 过滤掉为”0“的字节了
            // TODO
            String deviceId = byteToStr(segment_3);
            logger.debug("deviceId: " + deviceId);
            // 计算下一个段开始的位置
            begin = begin + count;

            // 电流 A-C 数据开始
            // 电流A
            // 长度： 2字节，count = 2; 128个点， 2个周期， 共 2*128*2=512个字节 , 2字节转short
            // 使用Bigdecimal 接收数据， 计算不丢失精度
            count = 2;
            // TODO current_A_1需要傅里叶变换， 取结果，
            // TODO current_A_2  周期2不需要进行傅里叶变换，直接存储
            BigDecimal[] current_A_1 = new BigDecimal[128];
            BigDecimal[] current_A_2 = new BigDecimal[128];
            // 电流要 除以2000 才是实际的值
            BigDecimal divide2000 = new BigDecimal("2000");
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                // BigDecimal  divide()有 scale 和 precision 的概念，scale 表示小数点右边的位数，而 precision 表示精度，也就是有效数字的长度。
                current_A_1[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide2000, 4, RoundingMode.HALF_UP);
                begin = begin + count;
            }
            // 电流A 周期1 进行傅里叶转换
            DoubleFFT_1D dftA1 = new DoubleFFT_1D(128);
            // Bigdecimal 数组转为double数组
            double[] currentA1 = new double[current_A_1.length];
            for (int i = 0; i < current_A_1.length; i++) {
                currentA1[i] = current_A_1[i].doubleValue();
            }
            // 傅里叶转换
            dftA1.realForward(currentA1);
            // 源数据被改变， 获取虚数和实数两个部分
            double[] realPartA1 = new double[currentA1.length / 2];
            double[] imaginaryPartA1 = new double[currentA1.length / 2];
            for (int i = 0; i < currentA1.length / 2; i++) {
                realPartA1[i] = currentA1[2 * i];
                imaginaryPartA1[i] = currentA1[2 * i + 1];
            }
            // 结果取模： （实数平方+ 虚数平方）开根号：
            // TODO 周期1处理的结果， 用于绘图 cur_fft
            double[] cur_fftA1 = new double[realPartA1.length];
            for (int i = 0; i < realPartA1.length; i++) {
                cur_fftA1[i] = Math.sqrt(Math.pow(realPartA1[i], 2) + Math.pow(imaginaryPartA1[i], 2));
            }
            // 周期2开始：
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                current_A_2[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide2000, 4, RoundingMode.HALF_UP);
                begin = begin + count;
            }

            // 电流B
            // 长度： 2字节，count = 2; 128个点， 2个周期， 共 2*128*2=512个字节 , 2字节转short
            // 使用Bigdecimal 接收数据， 计算不丢失精度
            count = 2;
            // TODO current_B_1 需要傅里叶变换， 取结果，
            // TODO current_B_2 周期2不需要进行傅里叶变换，直接存储
            BigDecimal[] current_B_1 = new BigDecimal[128];
            BigDecimal[] current_B_2 = new BigDecimal[128];
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                // 电流要 除以2000 才是实际的值
                // BigDecimal  divide()有 scale 和 precision 的概念，scale 表示小数点右边的位数，而 precision 表示精度，也就是有效数字的长度。
                current_B_1[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide2000, 4, RoundingMode.HALF_UP);
                begin = begin + count;
            }
            // 电流B 周期1 进行傅里叶转换
            DoubleFFT_1D dftB1 = new DoubleFFT_1D(128);
            // Bigdecimal 数组转为double数组
            double[] currentB1 = new double[current_B_1.length];
            for (int i = 0; i < current_B_1.length; i++) {
                currentB1[i] = current_B_1[i].doubleValue();
            }
            // 傅里叶转换
            dftB1.realForward(currentB1);
            // 源数据被改变， 获取虚数和实数两个部分
            double[] realPartB1 = new double[currentB1.length / 2];
            double[] imaginaryPartB1 = new double[currentB1.length / 2];
            for (int i = 0; i < currentB1.length / 2; i++) {
                realPartB1[i] = currentB1[2 * i];
                imaginaryPartB1[i] = currentB1[2 * i + 1];
            }
            // 结果取模： （实数平方+ 虚数平方）开根号：
            // TODO 周期1处理的结果， 用于绘图 cur_fft
            double[] cur_fftB1 = new double[realPartB1.length];
            for (int i = 0; i < realPartB1.length; i++) {
                cur_fftB1[i] = Math.sqrt(Math.pow(realPartB1[i], 2) + Math.pow(imaginaryPartB1[i], 2));
            }
            // 周期2开始：
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                // 电流要 除以2000 才是实际的值
                // BigDecimal  divide()有 scale 和 precision 的概念，scale 表示小数点右边的位数，而 precision 表示精度，也就是有效数字的长度。
                current_B_2[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide2000, 4, RoundingMode.HALF_UP);
                begin = begin + count;
            }

            // 电流C
            // 长度： 2字节，count = 2; 128个点， 2个周期， 共 2*128*2=512个字节 , 2字节转short
            // 使用Bigdecimal 接收数据， 计算不丢失精度
            count = 2;
            // TODO current_C_1 需要傅里叶变换， 取结果，
            // TODO current_C_2 周期2不需要进行傅里叶变换，直接存储
            BigDecimal[] current_C_1 = new BigDecimal[128];
            BigDecimal[] current_C_2 = new BigDecimal[128];
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                // 电流要 除以2000 才是实际的值
                // BigDecimal  divide()有 scale 和 precision 的概念，scale 表示小数点右边的位数，而 precision 表示精度，也就是有效数字的长度。
                current_C_1[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide2000, 4, RoundingMode.HALF_UP);
                begin = begin + count;
            }
            // 电流C 周期1 进行傅里叶转换
            DoubleFFT_1D dftC1 = new DoubleFFT_1D(128);
            // Bigdecimal 数组转为double数组
            double[] currentC1 = new double[current_C_1.length];
            for (int i = 0; i < current_C_1.length; i++) {
                currentC1[i] = current_C_1[i].doubleValue();
            }
            // 傅里叶转换
            dftC1.realForward(currentC1);
            // 源数据被改变， 获取虚数和实数两个部分
            double[] realPartC1 = new double[currentC1.length / 2];
            double[] imaginaryPartC1 = new double[currentC1.length / 2];
            for (int i = 0; i < currentC1.length / 2; i++) {
                realPartC1[i] = currentC1[2 * i];
                imaginaryPartC1[i] = currentC1[2 * i + 1];
            }
            // 结果取模： （实数平方+ 虚数平方）开根号：
            // TODO 周期1处理的结果， 用于绘图 cur_fftC1
            double[] cur_fftC1 = new double[realPartC1.length];
            for (int i = 0; i < realPartC1.length; i++) {
                cur_fftC1[i] = Math.sqrt(Math.pow(realPartC1[i], 2) + Math.pow(imaginaryPartC1[i], 2));
            }
            // 周期2开始：
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                // 电流要 除以2000 才是实际的值
                // BigDecimal  divide()有 scale 和 precision 的概念，scale 表示小数点右边的位数，而 precision 表示精度，也就是有效数字的长度。
                current_C_2[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide2000, 4, RoundingMode.HALF_UP);
                begin = begin + count;
            }

            // 电压A
            // 长度： 2字节，count = 2; 128个点， 2个周期， 共 2*128*2=512个字节 , 2字节转short
            // TODO voltage_A_1 voltage_A_2
            BigDecimal[] voltage_A_1 = new BigDecimal[128];
            BigDecimal[] voltage_A_2 = new BigDecimal[128];
            // 电流要 除以50 才是实际的值
            BigDecimal divide50 = new BigDecimal("50");
            count = 2;
            // 第1个周期
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                voltage_A_1[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide50, 4, RoundingMode.HALF_UP);
                begin = begin + count;
            }
            // 第2个周期
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                voltage_A_2[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide50, 4, RoundingMode.HALF_UP);
                begin = begin + count;
            }

            // 电压B
            // 长度： 2字节， 128个点， 2个周期， 共 2*128*2=512个字节 , 2字节转short
            // TODO voltage_B_1 voltage_B_2
            BigDecimal[] voltage_B_1 = new BigDecimal[128];
            BigDecimal[] voltage_B_2 = new BigDecimal[128];
            count = 2;
            // 第1个周期
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                voltage_B_1[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide50, 4, RoundingMode.HALF_UP);
                begin = begin + count;
            }
            // 第2个周期
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                voltage_B_2[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide50, 4, RoundingMode.HALF_UP);
                begin = begin + count;
            }

            // 电压C
            // 长度： 2字节， 128个点， 2个周期， 共 2*128*2=512个字节 , 2字节转short
            // TODO voltage_C_1 voltage_C_2
            BigDecimal[] voltage_C_1 = new BigDecimal[128];
            BigDecimal[] voltage_C_2 = new BigDecimal[128];
            count = 2;
            // 第1个周期
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                voltage_C_1[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide50, 4, RoundingMode.HALF_UP);
                begin = begin + count;
            }
            // 第2个周期
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                voltage_C_2[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide50, 4, RoundingMode.HALF_UP);
                begin = begin + count;
            }

            // end
            // 第一段报文结束， 结束位 2字节， 校验 0D 0A
            count = 2;
            byte[] segment_4 = subBytes(bytes, begin, count);
            String str4 = bytesToHexString(segment_4);
            // 解析成16进制， 判断是否合规。
            if (!(StringUtils.equals(str4, "0D0A") || StringUtils.equals(str4, "0d0a"))) {
                logger.debug("第【1】段数据解析错误， 期望的数据头为 0D0A 或 0d0a， 得到的文件头为: " + str4);
                return;
            }
            logger.debug("第【1】段数据结束位校验通过，解析完成 <--");
            begin = begin + count;

            // 第二段报文开始
            // header
            // 2字节 不使用 68 3F
            count = 2;
            // 在全部字节数组中， 截取头部2字节
            byte[] segment_5 = subBytes(bytes, begin, count);
            // 解析成16进制， 判断是否合规。
            String str5 = bytesToHexString(segment_5);
            if (!(StringUtils.equals(str5, "683f") || StringUtils.equals(str5, "683F"))) {
                logger.debug("第【2】段数据解析错误，期望的数据头为 683F 或 683f， 得到的文件头为: " + str1);
                return;
            }
            // 计算下一个段开始的位置
            begin = begin + count;
            logger.debug("第【2】段数据头校验通过，开始解析数据-->");

            // timestamp
            // 4字节，count = 4; 不使用， 上传的时间戳（二进制解析成10进制， 数值应该是错误的， 或者硬件时间没有调整）
            // 第二段的时间戳和第一段相同， 跳过解析
            count = 4;
            begin = begin + count;

            // 设备ID
            // 16 字节， 实际有2字节是 0； count = 16; 转ASCII， 文本
            // 第二段的时间戳和第一段相同， 跳过解析
            count = 16;
            begin = begin + count;

            // 有功功率
            // 4 字节 * 4 分辨率:0.01w 转int
            count = 4;
            // TODO activePower
            BigDecimal[] activePower = new BigDecimal[4];
            BigDecimal multiply001 = new BigDecimal("0.01");
            for (int i = 0; i < 4; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                activePower[i] = new BigDecimal(bytesToInt(segment_i)).multiply(multiply001);
                begin = begin + count;
            }

            // 无功功率
            // 4 字节 * 4  分辨率:0.01var 转int
            count = 4;
            // TODO reactivePower
            BigDecimal[] reactivePower = new BigDecimal[4];
            for (int i = 0; i < 4; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                reactivePower[i] = new BigDecimal(bytesToInt(segment_i)).multiply(multiply001);
                begin = begin + count;
            }

            // 功率因数
            // 4 字节 * 4 分辨率:0.001 转int
            count = 4;
            // TODO powerFactor
            BigDecimal[] powerFactor = new BigDecimal[4];
            BigDecimal multiply0001 = new BigDecimal("0.001");
            for (int i = 0; i < 4; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                powerFactor[i] = new BigDecimal(bytesToInt(segment_i)).multiply(multiply0001);
                begin = begin + count;
            }

            // A相谐波电压
            // 长度： 2字节， 62个点，分辨率： 0.01 单位：% 共 2*62=124个字节 , 2字节转short
            count = 2;
            // TODO harmonic_V_A
            BigDecimal[] harmonic_V_A = new BigDecimal[62];
            for (int i = 0; i < 62; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                harmonic_V_A[i] = new BigDecimal(bytesToShort(segment_i)).multiply(multiply001);
                begin = begin + count;
            }

            // B相谐波电压
            // 长度： 2字节， 62个点，分辨率： 0.01 单位：% 共 2*62=124个字节 , 2字节转short
            count = 2;
            // TODO harmonic_V_B
            BigDecimal[] harmonic_V_B = new BigDecimal[62];
            for (int i = 0; i < 62; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                harmonic_V_B[i] = new BigDecimal(bytesToShort(segment_i)).multiply(multiply001);
                begin = begin + count;
            }

            // C相谐波电压
            // 长度： 2字节， 62个点，分辨率： 0.01 单位：% 共 2*62=124个字节 , 2字节转short
            count = 2;
            // TODO harmonic_V_C
            BigDecimal[] harmonic_V_C = new BigDecimal[62];
            for (int i = 0; i < 62; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                harmonic_V_C[i] = new BigDecimal(bytesToShort(segment_i)).multiply(multiply001);
                begin = begin + count;
            }

            // A相谐波电流
            // 长度： 2字节， 62个点，分辨率： 0.01 单位：% 共 2*62=124个字节 , 2字节转short
            count = 2;
            // TODO harmonic_C_A
            BigDecimal[] harmonic_C_A = new BigDecimal[62];
            for (int i = 0; i < 62; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                harmonic_C_A[i] = new BigDecimal(bytesToShort(segment_i)).multiply(multiply001);
                begin = begin + count;
            }
            // B相谐波电流
            // 长度： 2字节， 62个点，分辨率： 0.01 单位：% 共 2*62=124个字节 , 2字节转short
            count = 2;
            // TODO harmonic_C_B
            BigDecimal[] harmonic_C_B = new BigDecimal[62];
            for (int i = 0; i < 62; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                harmonic_C_B[i] = new BigDecimal(bytesToShort(segment_i)).multiply(multiply001);
                begin = begin + count;
            }

            // C相谐波电流
            // 长度： 2字节， 62个点，分辨率： 0.01 单位：% 共 2*62=124个字节 , 2字节转short
            count = 2;
            // TODO harmonic_C_C
            BigDecimal[] harmonic_C_C = new BigDecimal[62];
            for (int i = 0; i < 62; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                harmonic_C_C[i] = new BigDecimal(bytesToShort(segment_i)).multiply(multiply001);
                begin = begin + count;
            }

            // end
            // 第二段报文结束， 结束位 2字节， 校验 0D 0A
            count = 2;
            byte[] segment_11 = subBytes(bytes, begin, count);
            String str11 = bytesToHexString(segment_11);
            // 解析成16进制， 判断是否合规。
            if (!(StringUtils.equals(str11, "0D0A") || StringUtils.equals(str11, "0d0a"))) {
                logger.debug("第【1】段数据解析错误， 期望的数据头为 0D0A 或 0d0a， 得到的文件头为: " + str11);
                return;
            }
            logger.debug("第【2】段数据结束位校验通过，解析完成 <--");

            // 向客户端回写数据
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 出现异常进行的处理
     *
     * @param ctx   ChannelHandlerContext
     * @param cause Throwable
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.debug("出现异常停止运行...");
        cause.printStackTrace();
        ctx.close();
    }

    //byte[]转int和short方法
    //16位即两字节的举例
    //0x38，0xff 对应 -200 ，
    //使用 byte[] aa={0x38,(byte)0xff};  bytesToShort(aa);
    //32位即四字节的举例  0x12, 0x34, 0x56, 0x78 对应 2018915346
    //使用 byte[] aa={0x12, 0x34, 0x56, 0x78};  bytesToInt(aa);
    public static byte[] intToBytes(int a) {
        byte[] ans = new byte[4];
        for (int i = 0; i < 4; i++)
            ans[i] = (byte) (a >> (i * 8));//截断 int 的低 8 位为一个字节 byte，并存储起来
        return ans;
    }

    public static int bytesToInt(byte[] a) {
        int ans = 0;
        for (int i = 0; i < 4; i++) {
            ans <<= 8;
            ans |= (a[3 - i] & 0xff);
        }
        return ans;
    }

    public static byte[] shortToBytes(short a) {
        byte[] ans = new byte[2];
        for (int i = 0; i < 2; i++)
            ans[i] = (byte) (a >> (i * 8));//截断 int 的低 8 位为一个字节 byte，并存储起来
        return ans;
    }

    public static short bytesToShort(byte[] a) {
        short ans = 0;
        for (int i = 0; i < 2; i++) {
            ans <<= 8;
            ans |= (a[1 - i] & 0xff);
        }
        return ans;
    }

    /**
     * @param src begin(起始位置) count(长度)
     * @return byte[]
     * @description: 截取数据长度
     * @author Shawn
     * @date 2024/03/23 23:19
     */
    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        for (int i = begin; i < begin + count; i++) {
            bs[i - begin] = src[i];
        }
        return bs;
    }

    /**
     * 16进制直接转换成为字符串(无需Unicode解码)
     *
     * @param hexStr
     * @return
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    /**
     * byte转换成为16进制String
     *
     * @param bArray
     * @return
     */
    public String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 去掉byte[]中填充的0 转为String
     *
     * @param buffer
     * @return
     */
    public static String byteToStr(byte[] buffer) {
        try {
            int length = 0;
            for (int i = 0; i < buffer.length; ++i) {
                if (buffer[i] == 0) {
                    length = i;
                    break;
                }
            }
            return new String(buffer, 0, length, "UTF-8");
        } catch (Exception e) {
            return "";
        }
    }
}
