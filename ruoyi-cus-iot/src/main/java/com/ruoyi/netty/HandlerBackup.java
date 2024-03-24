package com.ruoyi.netty;

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
public class HandlerBackup extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            //  TODO 执行拿到数据后的操作
            //@SuppressWarnings("unchecked")
            byte[] bytes = (byte[]) msg;
//            String str = bytesToHexString(bytes);
//            logger.debug("解包后的数据（16进制数据）: " + str);
//            logger.debug("解包后的数据长度（16进制数据）: " + str.length());
            logger.info("解包后的数据长度（字节数据）: " + bytes.length);

            // 开始解析数据
            int begin = 0;
            int count = 0;

            // header 2字节 不使用
//            logger.debug("header begin: " + begin);
            count = 2;
//            byte[] segment_1 = subBytes(bytes, begin, count);
//            String str1 = bytesToHexString(segment_1);
//            logger.debug("str1: " + str1);
            begin = begin + count;

            // timestamp 4字节， 不使用， 上传的时间戳（二进制解析成10进制）
            count = 4;
//            logger.debug("timestamp begin: " + begin);
            byte[] segment_2 = subBytes(bytes, begin, count);
            int str2 = bytesToInt(segment_2);
            logger.debug("str2: " + str2);
            begin = begin + count;

            // 设备ID  16 字节 转ASCII， 文本
            count = 16;
//            logger.debug("设备ID begin: " + begin);
            byte[] segment_3 = subBytes(bytes, begin, count);
            //Convert back to String
//            String deviceId = new String(segment_3);
            String deviceId = byteToStr(segment_3);
            logger.debug("deviceId: " + deviceId);
            begin = begin + count;

            // 电流A 长度： 2字节， 128个点， 2个周期， 共 2*128*2=512个字节 , 2字节转short
//            logger.debug("电流A begin: " + begin);
            BigDecimal[] current_A_1 = new BigDecimal[128];
            BigDecimal[] current_A_2 = new BigDecimal[128];
            count = 2;
            BigDecimal divide2000 = new BigDecimal("2000");
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                // BigDecimal  divide()有 scale 和 precision 的概念，scale 表示小数点右边的位数，而 precision 表示精度，也就是有效数字的长度。
                current_A_1[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide2000, 4, RoundingMode.HALF_UP);
                begin = begin + count;
            }
            logger.debug("电流A 周期1: " + Arrays.toString(current_A_1));

            //傅里叶转换
            DoubleFFT_1D dft = new DoubleFFT_1D(128);
            // Bigdecimal 数组转为double数组
            double[] currentA1 = new double[current_A_1.length];
            for (int i = 0; i < current_A_1.length; i++) {
                currentA1[i] = current_A_1[i].doubleValue();
            }
            // 转换
            dft.realForward(currentA1);
            // 源数据被改变， 获取虚数和实数两个部分
            double[] realPart = new double[currentA1.length / 2];
            double[] imaginaryPart = new double[currentA1.length / 2];
            for (int i = 0; i < currentA1.length / 2; i++) {
                realPart[i] = currentA1[2 * i];
                imaginaryPart[i] = currentA1[2 * i + 1];
            }
//            System.out.println("----------转换后  realPart----------");
//            System.out.println(Arrays.toString(realPart));
//            System.out.println("----------转换后   imaginaryPart----------");
//            System.out.println(Arrays.toString(imaginaryPart));

            // （实数平方+ 虚数平方）开根号：
            double[] cur_fft = new double[realPart.length];
            for(int i = 0; i< realPart.length; i++){
                cur_fft[i] = Math.sqrt(Math.pow(realPart[i], 2) + Math.pow(imaginaryPart[i],2));
            }
            System.out.println("----------（实数平方+ 虚数平方）开根号后：----------");
            System.out.println(Arrays.toString(cur_fft));

            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                current_A_2[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide2000, 4, RoundingMode.HALF_UP);
                begin = begin + count;
            }
            logger.debug("电流A: 周期2: " + Arrays.toString(current_A_2));

            // 电流B 长度： 2字节， 128个点， 2个周期， 共 2*128*2=512个字节 , 2字节转short
//            logger.debug("电流B begin: " + begin);
            BigDecimal[] current_B_1 = new BigDecimal[128];
            BigDecimal[] current_B_2 = new BigDecimal[128];
            count = 2;
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                current_B_1[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide2000, 4, RoundingMode.HALF_UP);
                begin = begin + count;
            }
            logger.debug("电流B 周期1: " + Arrays.toString(current_B_1));
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                current_B_2[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide2000, 4, RoundingMode.HALF_UP);
                begin = begin + count;
            }
            logger.debug("电流B: 周期2: " + Arrays.toString(current_B_2));

            // 电流C 长度： 2字节， 128个点， 2个周期， 共 2*128*2=512个字节 , 2字节转short
//            logger.debug("电流C begin: " + begin);
            BigDecimal[] current_C_1 = new BigDecimal[128];
            BigDecimal[] current_C_2 = new BigDecimal[128];
            count = 2;
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                current_C_1[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide2000, 4, RoundingMode.HALF_UP);
                begin = begin + count;
            }
            logger.debug("电流C 周期1: " + Arrays.toString(current_C_1));
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                current_C_2[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide2000, 4, RoundingMode.HALF_UP);
                begin = begin + count;
            }
            logger.debug("电流C: 周期2: " + Arrays.toString(current_C_2));

            // 电压A 长度： 2字节， 128个点， 2个周期， 共 2*128*2=512个字节 , 2字节转short
//            logger.debug("电压A begin: " + begin);
            BigDecimal[] voltage_A_1 = new BigDecimal[128];
            BigDecimal[] voltage_A_2 = new BigDecimal[128];
            BigDecimal divide50 = new BigDecimal("50");
            count = 2;
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                voltage_A_1[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide50, 4, RoundingMode.HALF_UP);
                begin = begin + count;
            }
            logger.debug("电压A 周期1: " + Arrays.toString(voltage_A_1));
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                voltage_A_2[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide50, 4, RoundingMode.HALF_UP);
                begin = begin + count;
            }
            logger.debug("电压A: 周期2: " + Arrays.toString(voltage_A_2));

            // 电压B 长度： 2字节， 128个点， 2个周期， 共 2*128*2=512个字节 , 2字节转short
//            logger.debug("电压B begin: " + begin);
            BigDecimal[] voltage_B_1 = new BigDecimal[128];
            BigDecimal[] voltage_B_2 = new BigDecimal[128];
            count = 2;
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                voltage_B_1[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide50, 4, RoundingMode.HALF_UP);
                begin = begin + count;
            }
            logger.debug("电压B 周期1: " + Arrays.toString(voltage_B_1));
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                voltage_B_2[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide50, 4, RoundingMode.HALF_UP);
                begin = begin + count;
            }
            logger.debug("电压B: 周期2: " + Arrays.toString(voltage_B_2));

            // 电压C 长度： 2字节， 128个点， 2个周期， 共 2*128*2=512个字节 , 2字节转short
//            logger.debug("电压C begin: " + begin);
            BigDecimal[] voltage_C_1 = new BigDecimal[128];
            BigDecimal[] voltage_C_2 = new BigDecimal[128];
            count = 2;
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                voltage_C_1[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide50, 4, RoundingMode.HALF_UP);
                begin = begin + count;
            }
            logger.debug("电压C 周期1: " + Arrays.toString(voltage_C_1));
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                voltage_C_2[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide50, 4, RoundingMode.HALF_UP);
                begin = begin + count;
            }
            logger.debug("电压C: 周期2: " + Arrays.toString(voltage_C_2));

            // 结束位 2字节， 校验
            // 0D 0A
            logger.debug("结束位 begin: " + begin);
            count = 2;
            byte[] segment_4 = subBytes(bytes, begin, count);
            String str4 = bytesToHexString(segment_4);
            logger.debug("结束位标识符 0D 0A: " + str4);
            begin = begin + count;

            // header 2字节 不使用
            // 68 3F
//            logger.debug("header begin: " + begin);
            count = 2;
//            byte[] segment_5 = subBytes(bytes, begin, count);
//            String str5 = bytesToHexString(segment_5);
//            logger.debug("str5: " + str5);
            begin = begin + count;

            // timestamp 4字节， 不使用，
//            logger.debug("timestamp begin: " + begin);
            count = 4;
//            byte[] segment_6 = subBytes(bytes, begin, count);
//            String str6 = bytesToHexString(segment_6);
//            logger.debug("str6: " + str6);
            begin = begin + count;

            // 设备ID  16 字节 转ASCII， 文本
//            logger.debug("设备ID begin: " + begin);
            count = 16;
            byte[] segment_7 = subBytes(bytes, begin, count);
            //Convert back to String
            String deviceId2 = byteToStr(segment_7);
            logger.debug("deviceId2: " + deviceId2);
            begin = begin + count;

            // 有功功率  4 字节 * 4 分辨率:0.01w 转int
//            logger.debug("有功功率 begin: " + begin);
            count = 4;
            BigDecimal[] activePower = new BigDecimal[4];
            BigDecimal multiply001 = new BigDecimal("0.01");
            for (int i = 0; i < 4; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                activePower[i] = new BigDecimal(bytesToInt(segment_i)).multiply(multiply001);
                begin = begin + count;
            }
            logger.debug("有功功率: " + Arrays.toString(activePower));

            // 无功功率  4 字节 分辨率:0.01var 转int
//            logger.debug("无功功率 begin: " + begin);
            count = 4;
            BigDecimal[] reactivePower = new BigDecimal[4];
            for (int i = 0; i < 4; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                reactivePower[i] = new BigDecimal(bytesToInt(segment_i)).multiply(multiply001);
                begin = begin + count;
            }
            logger.debug("无功功率: " + Arrays.toString(reactivePower));

            // 功率因数  4 字节 分辨率:0.001 转int
//            logger.debug("功率因数 begin: " + begin);
            count = 4;
            BigDecimal[] powerFactor = new BigDecimal[4];
            BigDecimal multiply0001 = new BigDecimal("0.001");
            for (int i = 0; i < 4; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                powerFactor[i] = new BigDecimal(bytesToInt(segment_i)).multiply(multiply0001);
                begin = begin + count;
            }
            logger.debug("功率因数: " + Arrays.toString(powerFactor));

            // A相谐波电压 长度： 2字节， 62个点，分辨率： 0.01 单位：% 共 2*62=124个字节 , 2字节转short
//            logger.debug("A相谐波电压 begin: " + begin);
            BigDecimal[] harmonic_V_A = new BigDecimal[62];
            count = 2;
            for (int i = 0; i < 62; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                harmonic_V_A[i] = new BigDecimal(bytesToShort(segment_i)).multiply(multiply001);
                begin = begin + count;
            }
            logger.debug("A相谐波电压: " + Arrays.toString(harmonic_V_A));

            // B相谐波电压 长度： 2字节， 62个点，分辨率： 0.01 单位：% 共 2*62=124个字节 , 2字节转short
//            logger.debug("B相谐波电压 begin: " + begin);
            BigDecimal[] harmonic_V_B = new BigDecimal[62];
            count = 2;
            for (int i = 0; i < 62; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                harmonic_V_B[i] = new BigDecimal(bytesToShort(segment_i)).multiply(multiply001);
                begin = begin + count;
            }
            logger.debug("B相谐波电压: " + Arrays.toString(harmonic_V_B));

            // C相谐波电压 长度： 2字节， 62个点，分辨率： 0.01 单位：% 共 2*62=124个字节 , 2字节转short
//            logger.debug("C相谐波电压 begin: " + begin);
            BigDecimal[] harmonic_V_C = new BigDecimal[62];
            count = 2;
            for (int i = 0; i < 62; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                harmonic_V_C[i] = new BigDecimal(bytesToShort(segment_i)).multiply(multiply001);
                begin = begin + count;
            }
            logger.debug("C相谐波电压: " + Arrays.toString(harmonic_V_C));

            // A相谐波电流 长度： 2字节， 62个点，分辨率： 0.01 单位：% 共 2*62=124个字节 , 2字节转short
//            logger.debug("A相谐波电流 begin: " + begin);
            BigDecimal[] harmonic_C_A = new BigDecimal[62];
            count = 2;
            for (int i = 0; i < 62; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                harmonic_C_A[i] = new BigDecimal(bytesToShort(segment_i)).multiply(multiply001);
                begin = begin + count;
            }
            logger.debug("A相谐波电流: " + Arrays.toString(harmonic_C_A));

            // B相谐波电流 长度： 2字节， 62个点，分辨率： 0.01 单位：% 共 2*62=124个字节 , 2字节转short
//            logger.debug("B相谐波电流 begin: " + begin);
            BigDecimal[] harmonic_C_B = new BigDecimal[62];
            count = 2;
            for (int i = 0; i < 62; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                harmonic_C_B[i] = new BigDecimal(bytesToShort(segment_i)).multiply(multiply001);
                begin = begin + count;
            }
            logger.debug("B相谐波电流: " + Arrays.toString(harmonic_C_B));

            // C相谐波电流 长度： 2字节， 62个点，分辨率： 0.01 单位：% 共 2*62=124个字节 , 2字节转short
            logger.debug("C相谐波电流 begin: " + begin);
            BigDecimal[] harmonic_C_C = new BigDecimal[62];
            count = 2;
            for (int i = 0; i < 62; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                harmonic_C_C[i] = new BigDecimal(bytesToShort(segment_i)).multiply(multiply001);
                begin = begin + count;
            }
            logger.debug("C相谐波电流 : " + Arrays.toString(harmonic_C_C));

            // 结束位 2字节， 校验
            // 0D 0A
            logger.debug("结束位 begin: " + begin);
            count = 2;
            byte[] segment_11 = subBytes(bytes, begin, count);
            String str11 = bytesToHexString(segment_11);
            logger.debug("结束位标识符 0D 0A: " + str11);


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
