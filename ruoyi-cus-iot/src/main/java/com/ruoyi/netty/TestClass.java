package com.ruoyi.netty;

import org.jtransforms.fft.FloatFFT_1D;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

public class TestClass {

    @Test
    public void test1(){
        String str = "0.0230, 0.0095, 0.0020, 0.0115, 0.0260, 0.0435, 0.0460, 0.2490, 0.8085, 0.9455, 0.7155, 0.4730, 0.2500, 0.1715, 0.2830, 0.3255, 0.2405, 0.1600, 0.1845, 0.1945, 0.2095, 0.2215, 0.2300, 0.2415, 0.2525, 0.2600, 0.2685, 0.2790, 0.3275, 0.7850, 1.1330, 1.1620, 1.0575, 0.7880, 0.5155, 0.4885, 0.4420, 0.3235, 0.3145, 0.3195, 0.3285, 0.3340, 0.3305, 0.3280, 0.3255, 0.3220, 0.3145, 0.3055, 0.2935, 0.2820, 0.2705, 0.2610, 0.2490, 0.2390, 0.2245, 0.2100, 0.1925, 0.1750, 0.1570, 0.1270, 0.0820, 0.0365, 0.0005, -0.0150, -0.0140, 0.0010, 0.0075, -0.0065, -0.0205, -0.0360, -0.0410, -0.2545, -0.7685, -0.9470, -0.6690, -0.4260, -0.2550, -0.1435, -0.2835, -0.3730, -0.2095, -0.1535, -0.1735, -0.1905, -0.2130, -0.2165, -0.2265, -0.2380, -0.2450, -0.2565, -0.2680, -0.2770, -0.3190, -0.7025, -1.1470, -1.1895, -0.9575, -0.7595, -0.5355, -0.4045, -0.4035, -0.3160, -0.3165, -0.3220, -0.3270, -0.3355, -0.3340, -0.3300, -0.3275, -0.3220, -0.3170, -0.3085, -0.2995, -0.2895, -0.2785, -0.2685, -0.2560, -0.2450, -0.2295, -0.2155, -0.1990, -0.1815, -0.1640, -0.1360, -0.0930, -0.0475, -0.0085, 0.0085";
        System.out.println(str.length());
        System.out.println(str.getBytes().length);
    }

    /*
     * 测试傅里叶变换
     *
     * */

    @Test
    public void testFFT() {

        float[] data = {0.023f, 0.0095f, 0.002f, 0.0115f, 0.026f, 0.0435f, 0.046f, 0.249f, 0.8085f, 0.9455f, 0.7155f, 0.473f, 0.25f, 0.1715f, 0.283f, 0.3255f, 0.2405f, 0.16f, 0.1845f, 0.1945f, 0.2095f, 0.2215f, 0.23f, 0.2415f, 0.2525f, 0.26f, 0.2685f, 0.279f, 0.3275f, 0.785f, 1.133f, 1.162f, 1.0575f, 0.788f, 0.5155f, 0.4885f, 0.442f, 0.3235f, 0.3145f, 0.3195f, 0.3285f, 0.334f, 0.3305f, 0.328f, 0.3255f, 0.322f, 0.3145f, 0.3055f, 0.2935f, 0.282f, 0.2705f, 0.261f, 0.249f, 0.239f, 0.2245f, 0.21f, 0.1925f, 0.175f, 0.157f, 0.127f, 0.082f, 0.0365f, 0.0005f, -0.015f, -0.014f, 0.001f, 0.0075f, -0.0065f, -0.0205f, -0.036f, -0.041f, -0.2545f, -0.7685f, -0.947f, -0.669f, -0.426f, -0.255f, -0.1435f, -0.2835f, -0.373f, -0.2095f, -0.1535f, -0.1735f, -0.1905f, -0.213f, -0.2165f, -0.2265f, -0.238f, -0.245f, -0.2565f, -0.268f, -0.277f, -0.319f, -0.7025f, -1.147f, -1.1895f, -0.9575f, -0.7595f, -0.5355f, -0.4045f, -0.4035f, -0.316f, -0.3165f, -0.322f, -0.327f, -0.3355f, -0.334f, -0.33f, -0.3275f, -0.322f, -0.317f, -0.3085f, -0.2995f, -0.2895f, -0.2785f, -0.2685f, -0.256f, -0.245f, -0.2295f, -0.2155f, -0.199f, -0.1815f, -0.164f, -0.136f, -0.093f, -0.0475f, -0.0085f, 0.0085f,};
        FloatFFT_1D dft = new FloatFFT_1D(128);
        System.out.println("----------转换前----------");
        System.out.println(Arrays.toString(data));

        dft.realForward(data);

        float[] realPart = new float[data.length / 2];
        float[] imaginaryPart = new float[data.length / 2];

        for (int i = 0; i < data.length / 2; i++) {
            realPart[i] = data[2 * i];
            imaginaryPart[i] = data[2 * i + 1];
        }

        // （实数平方+ 虚数平方）开根号：
        double[] cur_fft = new double[realPart.length];
        for(int i = 0; i< realPart.length; i++){
            float x = realPart[i];
            BigDecimal bx = new BigDecimal(String.valueOf(x));
            double dx = bx.doubleValue();
            float y = imaginaryPart[i];
            BigDecimal by = new BigDecimal(String.valueOf(y));
            double dy = by.doubleValue();
            cur_fft[i] = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy,2));
        }


        System.out.println("----------转换后----------");
        System.out.println(Arrays.toString(data));
        System.out.println("----------转换后  realPart----------");
        System.out.println(Arrays.toString(realPart));
        System.out.println("----------转换后   imaginaryPart----------");
        System.out.println(Arrays.toString(imaginaryPart));
        System.out.println("----------（实数平方+ 虚数平方）开根号后：----------");
        System.out.println(Arrays.toString(cur_fft));

//        Fourier fft = new Fourier(128, 6400);
//        //快速傅里叶变换
//        ComplexNumberArray cna = fft.fft(data1);
//        //输出结果
//        for (int i = 0; i < 128; i++)
//            System.out.println(cna.toString(i));
//        //用于对快速傅里叶变换的结果进行分析
//        Fourier.Analyzer fftAnalyzer = new Fourier.Analyzer(fft);
//        //输出最大幅值处的频率
//        System.out.println(fftAnalyzer.getFrequencyAtMaxAmplitude(cna));
//        //快速傅里叶逆变换
//        float[] oData = fft.ifft(cna);
//        //输出结果
//        for (float o : oData)
//            System.out.print(o + " , ");
    }


    /*
     * 测试2进制流读取解析成对应的数据
     *
     * */
    @Test
    public void temp1() {
        try {
            FileInputStream read = new FileInputStream(new File("D:\\10. 个人相关\\51.蔡长青\\array_byte_3912.dat"));
            byte[] bytes = new byte[read.available()];
            read.read(bytes);
            System.out.println(bytes.length);
            String str = bytesToHexString(bytes);
            System.out.println(str.length());

            int begin = 0;
            int count = 0;

            // header 2字节 不使用
            System.out.println("header begin: " + begin);
            count = 2;
            byte[] segment_1 = subBytes(bytes, begin, count);
            String str1 = bytesToHexString(segment_1);
            System.out.println("str1: " + str1);
            begin = begin + count;

            // timestamp 4字节， 不使用，
            count = 4;
            System.out.println("timestamp begin: " + begin);
            byte[] segment_2 = subBytes(bytes, begin, count);
            String str2 = bytesToHexString(segment_2);
            System.out.println("str2: " + str2);
            begin = begin + count;

            // 设备ID  16 字节 转ASCII， 文本
            count = 16;
            System.out.println("设备ID begin: " + begin);
            byte[] segment_3 = subBytes(bytes, begin, count);
            String str3 = bytesToHexString(segment_3);
            System.out.println("str3: " + str3);
            //Convert back to String
            String deviceId = new String(segment_3);
            System.out.println("deviceId: " + deviceId);
            begin = begin + count;

            // 电流A 长度： 2字节， 128个点， 2个周期， 共 2*128*2=512个字节 , 2字节转short
            System.out.println("电流A begin: " + begin);
            short[] current_A_1 = new short[128];
            short[] current_A_2 = new short[128];
            count = 2;
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                current_A_1[i] = bytesToShort(segment_i);
                begin = begin + count;
            }
            System.out.println("电流A 周期1: " + Arrays.toString(current_A_1));
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                current_A_2[i] = bytesToShort(segment_i);
                begin = begin + count;
            }
            System.out.println("电流A: 周期2: " + Arrays.toString(current_A_2));

            // 电流B 长度： 2字节， 128个点， 2个周期， 共 2*128*2=512个字节 , 2字节转short
            System.out.println("电流B begin: " + begin);
            short[] current_B_1 = new short[128];
            short[] current_B_2 = new short[128];
            count = 2;
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                current_B_1[i] = bytesToShort(segment_i);
                begin = begin + count;
            }
            System.out.println("电流B 周期1: " + Arrays.toString(current_B_1));
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                current_B_2[i] = bytesToShort(segment_i);
                begin = begin + count;
            }
            System.out.println("电流B: 周期2: " + Arrays.toString(current_B_2));

            // 电流C 长度： 2字节， 128个点， 2个周期， 共 2*128*2=512个字节 , 2字节转short
            System.out.println("电流C begin: " + begin);
            short[] current_C_1 = new short[128];
            short[] current_C_2 = new short[128];
            count = 2;
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                current_C_1[i] = bytesToShort(segment_i);
                begin = begin + count;
            }
            System.out.println("电流C 周期1: " + Arrays.toString(current_C_1));
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                current_C_2[i] = bytesToShort(segment_i);
                begin = begin + count;
            }
            System.out.println("电流C: 周期2: " + Arrays.toString(current_C_2));

            // 电压A 长度： 2字节， 128个点， 2个周期， 共 2*128*2=512个字节 , 2字节转short
            System.out.println("电压A begin: " + begin);
            short[] voltage_A_1 = new short[128];
            short[] voltage_A_2 = new short[128];
            count = 2;
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                voltage_A_1[i] = bytesToShort(segment_i);
                begin = begin + count;
            }
            System.out.println("电压A 周期1: " + Arrays.toString(voltage_A_1));
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                voltage_A_2[i] = bytesToShort(segment_i);
                begin = begin + count;
            }
            System.out.println("电压A: 周期2: " + Arrays.toString(voltage_A_2));

            // 电压B 长度： 2字节， 128个点， 2个周期， 共 2*128*2=512个字节 , 2字节转short
            System.out.println("电压B begin: " + begin);
            short[] voltage_B_1 = new short[128];
            short[] voltage_B_2 = new short[128];
            count = 2;
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                voltage_B_1[i] = bytesToShort(segment_i);
                begin = begin + count;
            }
            System.out.println("电压B 周期1: " + Arrays.toString(voltage_B_1));
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                voltage_B_2[i] = bytesToShort(segment_i);
                begin = begin + count;
            }
            System.out.println("电压B: 周期2: " + Arrays.toString(voltage_B_2));

            // 电压C 长度： 2字节， 128个点， 2个周期， 共 2*128*2=512个字节 , 2字节转short
            System.out.println("电压C begin: " + begin);
            short[] voltage_C_1 = new short[128];
            short[] voltage_C_2 = new short[128];
            count = 2;
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                voltage_C_1[i] = bytesToShort(segment_i);
                begin = begin + count;
            }
            System.out.println("电压C 周期1: " + Arrays.toString(voltage_C_1));
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                voltage_C_2[i] = bytesToShort(segment_i);
                begin = begin + count;
            }
            System.out.println("电压C: 周期2: " + Arrays.toString(voltage_C_2));

            // 结束位 2字节， 校验
            // 0D 0A
            System.out.println("结束位 begin: " + begin);
            count = 2;
            byte[] segment_4 = subBytes(bytes, begin, count);
            String str4 = bytesToHexString(segment_4);
            System.out.println("str4: " + str4);
            begin = begin + count;

            // header 2字节 不使用
            // 68 3F
            System.out.println("header begin: " + begin);
            count = 2;
            byte[] segment_5 = subBytes(bytes, begin, count);
            String str5 = bytesToHexString(segment_5);
            System.out.println("str5: " + str5);
            begin = begin + count;

            // timestamp 4字节， 不使用，
            System.out.println("timestamp begin: " + begin);
            count = 4;
            byte[] segment_6 = subBytes(bytes, begin, count);
            String str6 = bytesToHexString(segment_6);
            System.out.println("str6: " + str6);
            begin = begin + count;

            // 设备ID  16 字节 转ASCII， 文本
            System.out.println("设备ID begin: " + begin);
            count = 16;
            byte[] segment_7 = subBytes(bytes, begin, count);
            String str7 = bytesToHexString(segment_7);
            System.out.println("str7: " + str7);
            //Convert back to String
            String deviceId2 = new String(segment_7);
            System.out.println("deviceId2: " + deviceId2);
            begin = begin + count;

            // 有功功率  4 字节 * 4 分辨率:0.01w 转int
            System.out.println("有功功率 begin: " + begin);
            count = 4;
            int[] activePower = new int[4];
            for (int i = 0; i < 4; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                activePower[i] = bytesToInt(segment_i);
                begin = begin + count;
            }
            System.out.println("有功功率: " + Arrays.toString(activePower));

            // 无功功率  4 字节 分辨率:0.01var 转int
            System.out.println("无功功率 begin: " + begin);
            count = 4;
            int[] reactivePower = new int[4];
            for (int i = 0; i < 4; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                reactivePower[i] = bytesToInt(segment_i);
                begin = begin + count;
            }
            System.out.println("无功功率: " + Arrays.toString(reactivePower));

            // 功率因数  4 字节 分辨率:0.001 转int
            System.out.println("功率因数 begin: " + begin);
            count = 4;
            int[] powerFactor = new int[4];
            for (int i = 0; i < 4; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                powerFactor[i] = bytesToInt(segment_i);
                begin = begin + count;
            }
            System.out.println("功率因数: " + Arrays.toString(powerFactor));

            // A相谐波电压 长度： 2字节， 62个点，分辨率： 0.01 单位：% 共 2*62=124个字节 , 2字节转short
            System.out.println("A相谐波电压 begin: " + begin);
            short[] harmonic_V_A = new short[62];
            count = 2;
            for (int i = 0; i < 62; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                harmonic_V_A[i] = bytesToShort(segment_i);
                begin = begin + count;
            }
            System.out.println("A相谐波电压: " + Arrays.toString(harmonic_V_A));

            // B相谐波电压 长度： 2字节， 62个点，分辨率： 0.01 单位：% 共 2*62=124个字节 , 2字节转short
            System.out.println("B相谐波电压 begin: " + begin);
            short[] harmonic_V_B = new short[62];
            count = 2;
            for (int i = 0; i < 62; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                harmonic_V_B[i] = bytesToShort(segment_i);
                begin = begin + count;
            }
            System.out.println("B相谐波电压: " + Arrays.toString(harmonic_V_B));

            // C相谐波电压 长度： 2字节， 62个点，分辨率： 0.01 单位：% 共 2*62=124个字节 , 2字节转short
            System.out.println("C相谐波电压 begin: " + begin);
            short[] harmonic_V_C = new short[62];
            count = 2;
            for (int i = 0; i < 62; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                harmonic_V_C[i] = bytesToShort(segment_i);
                begin = begin + count;
            }
            System.out.println("C相谐波电压: " + Arrays.toString(harmonic_V_C));

            // A相谐波电流 长度： 2字节， 62个点，分辨率： 0.01 单位：% 共 2*62=124个字节 , 2字节转short
            System.out.println("A相谐波电流 begin: " + begin);
            short[] harmonic_C_A = new short[62];
            count = 2;
            for (int i = 0; i < 62; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                harmonic_C_A[i] = bytesToShort(segment_i);
                begin = begin + count;
            }
            System.out.println("A相谐波电流: " + Arrays.toString(harmonic_C_A));

            // B相谐波电流 长度： 2字节， 62个点，分辨率： 0.01 单位：% 共 2*62=124个字节 , 2字节转short
            System.out.println("B相谐波电流 begin: " + begin);
            short[] harmonic_C_B = new short[62];
            count = 2;
            for (int i = 0; i < 62; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                harmonic_C_B[i] = bytesToShort(segment_i);
                begin = begin + count;
            }
            System.out.println("B相谐波电流: " + Arrays.toString(harmonic_C_B));

            // C相谐波电流 长度： 2字节， 62个点，分辨率： 0.01 单位：% 共 2*62=124个字节 , 2字节转short
            System.out.println("C相谐波电流 begin: " + begin);
            short[] harmonic_C_C = new short[62];
            count = 2;
            for (int i = 0; i < 62; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                harmonic_C_C[i] = bytesToShort(segment_i);
                begin = begin + count;
            }
            System.out.println("C相谐波电流 : " + Arrays.toString(harmonic_C_C));

            // 结束位 2字节， 校验
            // 0D 0A
            System.out.println("结束位 begin: " + begin);
            count = 2;
            byte[] segment_11 = subBytes(bytes, begin, count);
            String str11 = bytesToHexString(segment_11);
            System.out.println("str11: " + str11);
            begin = begin + count;


        } catch (IOException e) {
            e.printStackTrace();
        }

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
     * @author panlupeng
     * @date 2021/12/1 10:40
     */
    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        for (int i = begin; i < begin + count; i++) {
            bs[i - begin] = src[i];
        }
        return bs;
    }

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
}
