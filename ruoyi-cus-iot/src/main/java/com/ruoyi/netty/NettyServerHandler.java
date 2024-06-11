package com.ruoyi.netty;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.iot.domain.*;
import com.ruoyi.iot.service.*;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.jtransforms.fft.DoubleFFT_1D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

@Component
@ChannelHandler.Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    @Autowired
    private ICusIoTCurrentService cusIoTCurrentService;
    @Autowired
    private ICusIotVoltageService cusIotVoltageService;
    @Autowired
    private ICusIotPowerDataService cusIotPowerDataService;
    @Autowired
    private ICusIotCurrentHarmonicService cusIotCurrentHarmonicService;
    @Autowired
    private ICusIotVoltageHarmonicService cusIotVoltageHarmonicService;
    @Autowired
    private ICusIotDeviceListService cusIotDeviceListService;
    @Autowired
    private ICusIotOriginalHistoryService cusIotOriginalHistoryService;
    @Autowired
    private RedisCache redisCache;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        // 6. 电流放大倍数： 读取系统参数值：cus.current.multiple
        BigDecimal currentMultiple = new BigDecimal(Convert.toStr(redisCache.getCacheObject(CacheConstants.SYS_CONFIG_KEY + "cus.current.multiple")));

        // 初始化, 生成UUID, e.g. ef21517ac0f94db4b464f7bf61f6b656
        String currentUUID = IdUtils.fastSimpleUUID();

        // 电流数据
        CusIoTCurrent cusIoTCurrent = new CusIoTCurrent();
        // 电压数据
        CusIotVoltage cusIotVoltage = new CusIotVoltage();
        // 功率数据 A:active_power:有功功率
        CusIotPowerData cusIotPowerDataA = new CusIotPowerData();
        cusIotPowerDataA.setType("P");
        // 功率数据 R:reactive_power:无功功率
        CusIotPowerData cusIotPowerDataR = new CusIotPowerData();
        cusIotPowerDataR.setType("Q");
        // 功率数据 F:power_factor:功率因素 consinΦ (phi)
        CusIotPowerData cusIotPowerDataF = new CusIotPowerData();
        cusIotPowerDataF.setType("C");
        // 谐波电压
        CusIotVoltageHarmonic cusIotVoltageHarmonic = new CusIotVoltageHarmonic();
        // 谐波电流
        CusIotCurrentHarmonic cusIotCurrentHarmonic = new CusIotCurrentHarmonic();
        // 设备列表
        CusIotDeviceList cusIotDeviceList = new CusIotDeviceList();
        // 将接收的字符转换成16进制，存入数据库
        CusIotOriginalHistory cusIotOriginalHistory = new CusIotOriginalHistory();

        // 设置唯一UUID， 使得每个表的数据都有唯一UUID关联，即同一个设备， 每推送一次数据，生成一个UUID进行关联， 该条数据的所有记录的UUID都相同
//        cusIotDeviceList.setLatestUUID(currentUUID);  这里需要在下面判断完是否存在以后进行赋值，否则无法查找设备ID
        cusIoTCurrent.setUUID(currentUUID);
        cusIotVoltage.setUUID(currentUUID);
        cusIotCurrentHarmonic.setUUID(currentUUID);
        cusIotVoltageHarmonic.setUUID(currentUUID);
        cusIotPowerDataA.setUUID(currentUUID);
        cusIotPowerDataR.setUUID(currentUUID);
        cusIotPowerDataF.setUUID(currentUUID);
        cusIotOriginalHistory.setUUID(currentUUID);

        // 获取提交的IP 地址
        InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String IP = ipSocket.getAddress().getHostAddress();
        // 电流数据来源IP地址
        cusIoTCurrent.setCreateBy(IP);
        // 电压数据来源IP地址
        cusIotVoltage.setCreateBy(IP);
        // 功率数据来源IP地址
        cusIotPowerDataA.setCreateBy(IP);
        cusIotPowerDataR.setCreateBy(IP);
        cusIotPowerDataF.setCreateBy(IP);
        // 谐波电压数据来源IP地址
        cusIotVoltageHarmonic.setCreateBy(IP);
        // 谐波电流数据来源IP地址
        cusIotCurrentHarmonic.setCreateBy(IP);
        cusIotOriginalHistory.setCreateBy(IP);

        try {
            byte[] bytes = (byte[]) msg;
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
                cusIotOriginalHistory.setData(bytesToHexString(bytes));
                cusIotOriginalHistory.setRemark("第【1】段数据解析错误， 期望的数据头为 683F 或 683f， 得到的文件头为: " + str1);
                cusIotOriginalHistoryService.insertCusIotOriginalHistory(cusIotOriginalHistory);
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
            // 字节转int, 高位在前， 与其他的不同
            int timestamp = bytesToIntHigh(segment_2);
            // 所有的表都需要设置这个timestamp字段
            // 电流数据
            cusIoTCurrent.setTimestamp((long) timestamp);
            // 电压数据
            cusIotVoltage.setTimestamp((long) timestamp);
            // 功率数据
            cusIotPowerDataA.setTimestamp((long) timestamp);
            cusIotPowerDataR.setTimestamp((long) timestamp);
            cusIotPowerDataF.setTimestamp((long) timestamp);
            // 谐波电压
            cusIotVoltageHarmonic.setTimestamp((long) timestamp);
            // 谐波电流
            cusIotCurrentHarmonic.setTimestamp((long) timestamp);
            // 解析原始数据记录
            cusIotOriginalHistory.setTimestamp((long) timestamp);

            // 计算下一个段开始的位置
            begin = begin + count;

            // 设备ID
            // 16 字节， 实际有2字节是 0； count = 16; 转ASCII， 文本
            count = 16;
            // 在字节数组中截取该段的字节
            byte[] segment_3 = subBytes(bytes, begin, count);
            // 字节转字符串， 过滤掉为”0“的字节了
            String deviceId = byteToStr(segment_3);
            // 所有的表都需要设置这个timestamp字段
            // 电流数据
            cusIoTCurrent.setDeviceId(deviceId);
            // 电压数据
            cusIotVoltage.setDeviceId(deviceId);
            // 有功功率
            cusIotPowerDataA.setDeviceId(deviceId);
            cusIotPowerDataR.setDeviceId(deviceId);
            cusIotPowerDataF.setDeviceId(deviceId);
            // 谐波电压
            cusIotVoltageHarmonic.setDeviceId(deviceId);
            // 谐波电流
            cusIotCurrentHarmonic.setDeviceId(deviceId);
            // 设备列表
            cusIotDeviceList.setDeviceId(deviceId);
            // 解析原始数据记录
            cusIotOriginalHistory.setDeviceId(deviceId);
            // 判断ID是否存在
            List<CusIotDeviceList> devices = cusIotDeviceListService.selectCusIotDeviceListList(cusIotDeviceList);
            if (devices.size() > 0) {
                // 设备列表
                cusIotDeviceList.setTimestamp((long) timestamp);
                // 设备列表IP
                cusIotDeviceList.setUpdateBy(IP);
                // 设置更新的主键ID
                cusIotDeviceList.setId(devices.get(0).getId());
                cusIotDeviceList.setLatestUUID(currentUUID);
                cusIotDeviceListService.updateCusIotDeviceList(cusIotDeviceList);
            } else {
                // 设备列表
                cusIotDeviceList.setTimestamp((long) timestamp);
                // 设备列表IP
                cusIotDeviceList.setCreateBy(IP);
                cusIotDeviceList.setLatestUUID(currentUUID);
                cusIotDeviceListService.insertCusIotDeviceList(cusIotDeviceList);
            }


            logger.debug("deviceId: " + deviceId);
            // 计算下一个段开始的位置
            begin = begin + count;

            // 电流 A-C 数据开始
            // 电流A
            // 长度： 2字节，count = 2; 128个点， 2个周期， 共 2*128*2=512个字节 , 2字节转short
            // 使用Bigdecimal 接收数据， 计算不丢失精度
            count = 2;
            //  current_A_1需要傅里叶变换， 取结果，
            //  current_A_2  周期2不需要进行傅里叶变换，直接存储
            String[] current_A_1 = new String[128];
            String[] current_A_2 = new String[128];
            // 电流要 除以2000 才是实际的值
            // 电流经过互感器， 缩小了10倍，所以需要再放大10倍， 2000改成了200.
            BigDecimal divide2000 = new BigDecimal("2000");
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                // BigDecimal  divide()有 scale 和 precision 的概念，scale 表示小数点右边的位数，而 precision 表示精度，也就是有效数字的长度。
                current_A_1[i] = new BigDecimal(bytesToShort(segment_i)).multiply(currentMultiple).divide(divide2000, 4, RoundingMode.HALF_UP).toPlainString();
                begin = begin + count;
            }
            cusIoTCurrent.setCurrentA1(Arrays.toString(current_A_1));
            // 电流A 周期1 进行傅里叶转换
            DoubleFFT_1D dftA1 = new DoubleFFT_1D(128);
            // Bigdecimal 数组转为double数组
            double[] currentA1 = new double[current_A_1.length];
            for (int i = 0; i < current_A_1.length; i++) {
                currentA1[i] = Double.parseDouble(current_A_1[i]);
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
            // 周期1处理的结果， 用于绘图 cur_fft
            String[] cur_fftA1 = new String[realPartA1.length];
            for (int i = 0; i < realPartA1.length; i++) {
                BigDecimal fftA1Value = BigDecimal.valueOf(Math.sqrt(Math.pow(realPartA1[i], 2) + Math.pow(imaginaryPartA1[i], 2)));
                // 保留4位小数
                cur_fftA1[i] = fftA1Value.setScale(4, RoundingMode.HALF_UP).toPlainString();
            }
            cusIoTCurrent.setCurrentAFFT(Arrays.toString(cur_fftA1));
            // 周期2开始：
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                current_A_2[i] = new BigDecimal(bytesToShort(segment_i)).multiply(currentMultiple).divide(divide2000, 4, RoundingMode.HALF_UP).toPlainString();
                begin = begin + count;
            }
            cusIoTCurrent.setCurrentA2(Arrays.toString(current_A_2));
            // 电流B
            // 长度： 2字节，count = 2; 128个点， 2个周期， 共 2*128*2=512个字节 , 2字节转short
            // 使用Bigdecimal 接收数据， 计算不丢失精度
            count = 2;
            // current_B_1 需要傅里叶变换， 取结果，
            // current_B_2 周期2不需要进行傅里叶变换，直接存储
            String[] current_B_1 = new String[128];
            String[] current_B_2 = new String[128];
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                // 电流要 除以2000 才是实际的值
                // BigDecimal  divide()有 scale 和 precision 的概念，scale 表示小数点右边的位数，而 precision 表示精度，也就是有效数字的长度。
                current_B_1[i] = new BigDecimal(bytesToShort(segment_i)).multiply(currentMultiple).divide(divide2000, 4, RoundingMode.HALF_UP).toPlainString();
                begin = begin + count;
            }
            cusIoTCurrent.setCurrentB1(Arrays.toString(current_B_1));
            // 电流B 周期1 进行傅里叶转换
            DoubleFFT_1D dftB1 = new DoubleFFT_1D(128);
            // Bigdecimal 数组转为double数组
            double[] currentB1 = new double[current_B_1.length];
            for (int i = 0; i < current_B_1.length; i++) {
                currentB1[i] = Double.parseDouble(current_B_1[i]);
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
            // 周期1处理的结果， 用于绘图 cur_fft
            String[] cur_fftB1 = new String[realPartB1.length];
            for (int i = 0; i < realPartB1.length; i++) {
                BigDecimal fftB1Value = new BigDecimal(Math.sqrt(Math.pow(realPartB1[i], 2) + Math.pow(imaginaryPartB1[i], 2)));
                // 保留4位小数
                cur_fftB1[i] = fftB1Value.setScale(4, RoundingMode.HALF_UP).toPlainString();
            }
            cusIoTCurrent.setCurrentBFFT(Arrays.toString(cur_fftB1));
            // 周期2开始：
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                // 电流要 除以2000 才是实际的值
                // BigDecimal  divide()有 scale 和 precision 的概念，scale 表示小数点右边的位数，而 precision 表示精度，也就是有效数字的长度。
                current_B_2[i] = new BigDecimal(bytesToShort(segment_i)).multiply(currentMultiple).divide(divide2000, 4, RoundingMode.HALF_UP).toPlainString();
                begin = begin + count;
            }
            cusIoTCurrent.setCurrentB2(Arrays.toString(current_B_2));
            // 电流C
            // 长度： 2字节，count = 2; 128个点， 2个周期， 共 2*128*2=512个字节 , 2字节转short
            // 使用Bigdecimal 接收数据， 计算不丢失精度
            count = 2;
            // current_C_1 需要傅里叶变换， 取结果，
            // current_C_2 周期2不需要进行傅里叶变换，直接存储
            String[] current_C_1 = new String[128];
            String[] current_C_2 = new String[128];
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                // 电流要 除以2000 才是实际的值
                // BigDecimal  divide()有 scale 和 precision 的概念，scale 表示小数点右边的位数，而 precision 表示精度，也就是有效数字的长度。
                current_C_1[i] = new BigDecimal(bytesToShort(segment_i)).multiply(currentMultiple).divide(divide2000, 4, RoundingMode.HALF_UP).toPlainString();
                begin = begin + count;
            }
            cusIoTCurrent.setCurrentC1(Arrays.toString(current_C_1));
            // 电流C 周期1 进行傅里叶转换
            DoubleFFT_1D dftC1 = new DoubleFFT_1D(128);
            // Bigdecimal 数组转为double数组
            double[] currentC1 = new double[current_C_1.length];
            for (int i = 0; i < current_C_1.length; i++) {
                currentC1[i] = Double.parseDouble(current_C_1[i]);
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
            // 周期1处理的结果， 用于绘图 cur_fftC1
            String[] cur_fftC1 = new String[realPartC1.length];
            for (int i = 0; i < realPartC1.length; i++) {
                BigDecimal fftC1 = BigDecimal.valueOf(Math.sqrt(Math.pow(realPartC1[i], 2) + Math.pow(imaginaryPartC1[i], 2)));
                // 保留4位小数
                cur_fftC1[i] = fftC1.setScale(4, RoundingMode.HALF_UP).toPlainString();
            }
            cusIoTCurrent.setCurrentCFFT(Arrays.toString(cur_fftC1));
            // 周期2开始：
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                // 电流要 除以2000 才是实际的值
                // BigDecimal  divide()有 scale 和 precision 的概念，scale 表示小数点右边的位数，而 precision 表示精度，也就是有效数字的长度。
                current_C_2[i] = new BigDecimal(bytesToShort(segment_i)).multiply(currentMultiple).divide(divide2000, 4, RoundingMode.HALF_UP).toPlainString();
                begin = begin + count;
            }
            cusIoTCurrent.setCurrentC2(Arrays.toString(current_C_2));

            // 电压A
            // 长度： 2字节，count = 2; 128个点， 2个周期， 共 2*128*2=512个字节 , 2字节转short
            // voltage_A_1 voltage_A_2
            String[] voltage_A_1 = new String[128];
            String[] voltage_A_2 = new String[128];
            // 电流要 除以50 才是实际的值
            BigDecimal divide50 = new BigDecimal("50");
            count = 2;
            // 第1个周期
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                voltage_A_1[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide50, 4, RoundingMode.HALF_UP).toPlainString();
                begin = begin + count;
            }
            cusIotVoltage.setVoltageA1(Arrays.toString(voltage_A_1));
            // Add Start 2024-04-16
            // 电压A 周期1 进行傅里叶转换
            DoubleFFT_1D dftVA1 = new DoubleFFT_1D(128);
            // Bigdecimal 数组转为double数组
            double[] voltageA1 = new double[voltage_A_1.length];
            for (int i = 0; i < voltage_A_1.length; i++) {
                voltageA1[i] = Double.parseDouble(voltage_A_1[i]);
            }
            // 傅里叶转换
            dftVA1.realForward(voltageA1);
            // 源数据被改变， 获取虚数和实数两个部分
            double[] realPartVA1 = new double[voltageA1.length / 2];
            double[] imaginaryPartVA1 = new double[voltageA1.length / 2];
            for (int i = 0; i < voltageA1.length / 2; i++) {
                realPartVA1[i] = voltageA1[2 * i];
                imaginaryPartVA1[i] = voltageA1[2 * i + 1];
            }
            // 结果取模： （实数平方+ 虚数平方）开根号：
            // 周期1处理的结果， 用于绘图 vol_fftA1
            String[] vol_fftA1 = new String[realPartVA1.length];
            for (int i = 0; i < realPartVA1.length; i++) {
                BigDecimal fftVA1 = BigDecimal.valueOf(Math.sqrt(Math.pow(realPartVA1[i], 2) + Math.pow(imaginaryPartVA1[i], 2)));
                // 保留4位小数
                vol_fftA1[i] = fftVA1.setScale(4, RoundingMode.HALF_UP).toPlainString();
            }
            cusIotVoltage.setVoltageAFFT(Arrays.toString(vol_fftA1));
            // Add end 2024-04-16
            // 第2个周期
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                voltage_A_2[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide50, 4, RoundingMode.HALF_UP).toPlainString();
                begin = begin + count;
            }
            cusIotVoltage.setVoltageA2(Arrays.toString(voltage_A_2));
            // 电压B
            // 长度： 2字节， 128个点， 2个周期， 共 2*128*2=512个字节 , 2字节转short
            // voltage_B_1 voltage_B_2
            String[] voltage_B_1 = new String[128];
            String[] voltage_B_2 = new String[128];
            count = 2;
            // 第1个周期
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                voltage_B_1[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide50, 4, RoundingMode.HALF_UP).toPlainString();
                begin = begin + count;
            }
            cusIotVoltage.setVoltageB1(Arrays.toString(voltage_B_1));
            // Add Start 2024-04-16
            // 电压B 周期1 进行傅里叶转换
            DoubleFFT_1D dftVB1 = new DoubleFFT_1D(128);
            // Bigdecimal 数组转为double数组
            double[] voltageB1 = new double[voltage_B_1.length];
            for (int i = 0; i < voltage_B_1.length; i++) {
                voltageB1[i] = Double.parseDouble(voltage_B_1[i]);
            }
            // 傅里叶转换
            dftVB1.realForward(voltageB1);
            // 源数据被改变， 获取虚数和实数两个部分
            double[] realPartVB1 = new double[voltageB1.length / 2];
            double[] imaginaryPartVB1 = new double[voltageB1.length / 2];
            for (int i = 0; i < voltageB1.length / 2; i++) {
                realPartVB1[i] = voltageB1[2 * i];
                imaginaryPartVB1[i] = voltageB1[2 * i + 1];
            }
            // 结果取模： （实数平方+ 虚数平方）开根号：
            // 周期1处理的结果， 用于绘图 vol_fftB1
            String[] vol_fftB1 = new String[realPartVB1.length];
            for (int i = 0; i < realPartVB1.length; i++) {
                BigDecimal fftVB1 = BigDecimal.valueOf(Math.sqrt(Math.pow(realPartVB1[i], 2) + Math.pow(imaginaryPartVB1[i], 2)));
                // 保留4位小数
                vol_fftB1[i] = fftVB1.setScale(4, RoundingMode.HALF_UP).toPlainString();
            }
            cusIotVoltage.setVoltageBFFT(Arrays.toString(vol_fftB1));
            // Add end 2024-04-16
            // 第2个周期
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                voltage_B_2[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide50, 4, RoundingMode.HALF_UP).toPlainString();
                begin = begin + count;
            }
            cusIotVoltage.setVoltageB2(Arrays.toString(voltage_B_2));
            // 电压C
            // 长度： 2字节， 128个点， 2个周期， 共 2*128*2=512个字节 , 2字节转short
            //  voltage_C_1 voltage_C_2
            String[] voltage_C_1 = new String[128];
            String[] voltage_C_2 = new String[128];
            count = 2;
            // 第1个周期
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                voltage_C_1[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide50, 4, RoundingMode.HALF_UP).toPlainString();
                begin = begin + count;
            }
            cusIotVoltage.setVoltageC1(Arrays.toString(voltage_C_1));
            // Add Start 2024-04-16
            // 电压C 周期1 进行傅里叶转换
            DoubleFFT_1D dftVC1 = new DoubleFFT_1D(128);
            // Bigdecimal 数组转为double数组
            double[] voltageC1 = new double[voltage_C_1.length];
            for (int i = 0; i < voltage_C_1.length; i++) {
                voltageC1[i] = Double.parseDouble(voltage_C_1[i]);
            }
            // 傅里叶转换
            dftVC1.realForward(voltageC1);
            // 源数据被改变， 获取虚数和实数两个部分
            double[] realPartVC1 = new double[voltageC1.length / 2];
            double[] imaginaryPartVC1 = new double[voltageC1.length / 2];
            for (int i = 0; i < voltageC1.length / 2; i++) {
                realPartVC1[i] = voltageC1[2 * i];
                imaginaryPartVC1[i] = voltageC1[2 * i + 1];
            }
            // 结果取模： （实数平方+ 虚数平方）开根号：
            // 周期1处理的结果， 用于绘图 vol_fftC1
            String[] vol_fftC1 = new String[realPartVC1.length];
            for (int i = 0; i < realPartVC1.length; i++) {
                BigDecimal fftVC1 = BigDecimal.valueOf(Math.sqrt(Math.pow(realPartVC1[i], 2) + Math.pow(imaginaryPartVC1[i], 2)));
                // 保留4位小数
                vol_fftC1[i] = fftVC1.setScale(4, RoundingMode.HALF_UP).toPlainString();
            }
            cusIotVoltage.setVoltageCFFT(Arrays.toString(vol_fftC1));
            // Add end 2024-04-16
            // 第2个周期
            for (int i = 0; i < 128; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                voltage_C_2[i] = new BigDecimal(bytesToShort(segment_i)).divide(divide50, 4, RoundingMode.HALF_UP).toPlainString();
                begin = begin + count;
            }
            cusIotVoltage.setVoltageC2(Arrays.toString(voltage_C_2));

            // end
            // 第一段报文结束， 结束位 2字节， 校验 0D 0A
            count = 2;
            byte[] segment_4 = subBytes(bytes, begin, count);
            String str4 = bytesToHexString(segment_4);
            // 解析成16进制， 判断是否合规。
            if (!(StringUtils.equals(str4, "0D0A") || StringUtils.equals(str4, "0d0a"))) {
                logger.debug("第【1】段数据解析错误， 期望的数据结束位为 0D0A 或 0d0a， 得到的文件头为: " + str4);
                cusIotOriginalHistory.setData(bytesToHexString(bytes));
                cusIotOriginalHistory.setRemark("第【1】段数据解析错误， 期望的数据结束位为 0D0A 或 0d0a， 得到的文件头为: " + str4);
                cusIotOriginalHistoryService.insertCusIotOriginalHistory(cusIotOriginalHistory);
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
                logger.debug("第【2】段数据解析错误，期望的数据头为 683F 或 683f， 得到的文件头为: " + str5);
                cusIotOriginalHistory.setData(bytesToHexString(bytes));
                cusIotOriginalHistory.setRemark("第【2】段数据解析错误，期望的数据头为 683F 或 683f， 得到的文件头为: " + str5);
                cusIotOriginalHistoryService.insertCusIotOriginalHistory(cusIotOriginalHistory);
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
            //  activePower
            BigDecimal multiply001 = new BigDecimal("0.01");
            BigDecimal activePowerA = new BigDecimal(bytesToInt(subBytes(bytes, begin, count))).multiply(multiply001);
            begin = begin + count;
            cusIotPowerDataA.setPhaseA(activePowerA.toPlainString());

            BigDecimal activePowerB = new BigDecimal(bytesToInt(subBytes(bytes, begin, count))).multiply(multiply001);
            begin = begin + count;
            cusIotPowerDataA.setPhaseB(activePowerB.toPlainString());

            BigDecimal activePowerC = new BigDecimal(bytesToInt(subBytes(bytes, begin, count))).multiply(multiply001);
            begin = begin + count;
            cusIotPowerDataA.setPhaseC(activePowerC.toPlainString());

            BigDecimal activePowerTotal = new BigDecimal(bytesToInt(subBytes(bytes, begin, count))).multiply(multiply001);
            begin = begin + count;
            cusIotPowerDataA.setTotal(activePowerTotal.toPlainString());

            // 无功功率
            // 4 字节 * 4  分辨率:0.01var 转int
            count = 4;
            // reactivePower
            BigDecimal reactivePowerA = new BigDecimal(bytesToInt(subBytes(bytes, begin, count))).multiply(multiply001);
            begin = begin + count;
            cusIotPowerDataR.setPhaseA(reactivePowerA.toPlainString());

            BigDecimal reactivePowerB = new BigDecimal(bytesToInt(subBytes(bytes, begin, count))).multiply(multiply001);
            begin = begin + count;
            cusIotPowerDataR.setPhaseB(reactivePowerB.toPlainString());

            BigDecimal reactivePowerC = new BigDecimal(bytesToInt(subBytes(bytes, begin, count))).multiply(multiply001);
            begin = begin + count;
            cusIotPowerDataR.setPhaseC(reactivePowerC.toPlainString());

            BigDecimal reactivePowerTotal = new BigDecimal(bytesToInt(subBytes(bytes, begin, count))).multiply(multiply001);
            begin = begin + count;
            cusIotPowerDataR.setTotal(reactivePowerTotal.toPlainString());

            // 功率因数
            // 4 字节 * 4 分辨率:0.001 转int
            count = 4;
            //  powerFactor
            BigDecimal multiply0001 = new BigDecimal("0.001");
            BigDecimal powerFactorA = new BigDecimal(bytesToInt(subBytes(bytes, begin, count))).multiply(multiply0001);
            begin = begin + count;
            cusIotPowerDataF.setPhaseA(powerFactorA.toPlainString());
            BigDecimal powerFactorB = new BigDecimal(bytesToInt(subBytes(bytes, begin, count))).multiply(multiply0001);
            begin = begin + count;
            cusIotPowerDataF.setPhaseB(powerFactorB.toPlainString());
            BigDecimal powerFactorC = new BigDecimal(bytesToInt(subBytes(bytes, begin, count))).multiply(multiply0001);
            begin = begin + count;
            cusIotPowerDataF.setPhaseC(powerFactorC.toPlainString());
            BigDecimal powerFactorTotal = new BigDecimal(bytesToInt(subBytes(bytes, begin, count))).multiply(multiply0001);
            begin = begin + count;
            cusIotPowerDataF.setTotal(powerFactorTotal.toPlainString());
            // A相谐波电压
            // 长度： 2字节， 62个点，分辨率： 0.01 单位：% 共 2*62=124个字节 , 2字节转short
            count = 2;
            // harmonic_V_A
            String[] harmonic_V_A = new String[62];
            for (int i = 0; i < 62; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                harmonic_V_A[i] = new BigDecimal(bytesToShort(segment_i)).multiply(multiply001).toPlainString();
                begin = begin + count;
            }
            cusIotVoltageHarmonic.setHarmonicA(Arrays.toString(harmonic_V_A));
            // B相谐波电压
            // 长度： 2字节， 62个点，分辨率： 0.01 单位：% 共 2*62=124个字节 , 2字节转short
            count = 2;
            // harmonic_V_B
            String[] harmonic_V_B = new String[62];
            for (int i = 0; i < 62; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                harmonic_V_B[i] = new BigDecimal(bytesToShort(segment_i)).multiply(multiply001).toPlainString();
                begin = begin + count;
            }
            cusIotVoltageHarmonic.setHarmonicB(Arrays.toString(harmonic_V_B));
            // C相谐波电压
            // 长度： 2字节， 62个点，分辨率： 0.01 单位：% 共 2*62=124个字节 , 2字节转short
            count = 2;
            // harmonic_V_C
            String[] harmonic_V_C = new String[62];
            for (int i = 0; i < 62; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                harmonic_V_C[i] = new BigDecimal(bytesToShort(segment_i)).multiply(multiply001).toPlainString();
                begin = begin + count;
            }
            cusIotVoltageHarmonic.setHarmonicC(Arrays.toString(harmonic_V_C));

            // A相谐波电流
            // 长度： 2字节， 62个点，分辨率： 0.01 单位：% 共 2*62=124个字节 , 2字节转short
            count = 2;
            // harmonic_C_A
            String[] harmonic_C_A = new String[62];
            for (int i = 0; i < 62; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                harmonic_C_A[i] = new BigDecimal(bytesToShort(segment_i)).multiply(multiply001).toPlainString();
                begin = begin + count;
            }
            cusIotCurrentHarmonic.setHarmonicA(Arrays.toString(harmonic_C_A));
            // B相谐波电流
            // 长度： 2字节， 62个点，分辨率： 0.01 单位：% 共 2*62=124个字节 , 2字节转short
            count = 2;
            // harmonic_C_B
            String[] harmonic_C_B = new String[62];
            for (int i = 0; i < 62; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                harmonic_C_B[i] = new BigDecimal(bytesToShort(segment_i)).multiply(multiply001).toPlainString();
                begin = begin + count;
            }
            cusIotCurrentHarmonic.setHarmonicB(Arrays.toString(harmonic_C_B));
            // C相谐波电流
            // 长度： 2字节， 62个点，分辨率： 0.01 单位：% 共 2*62=124个字节 , 2字节转short
            count = 2;
            // harmonic_C_C
            String[] harmonic_C_C = new String[62];
            for (int i = 0; i < 62; i++) {
                byte[] segment_i = subBytes(bytes, begin, count);
                harmonic_C_C[i] = new BigDecimal(bytesToShort(segment_i)).multiply(multiply001).toPlainString();
                begin = begin + count;
            }
            cusIotCurrentHarmonic.setHarmonicC(Arrays.toString(harmonic_C_C));


            // end
            // 第二段报文结束， 结束位 2字节， 校验 0D 0A
            count = 2;
            byte[] segment_11 = subBytes(bytes, begin, count);
            String str11 = bytesToHexString(segment_11);

            // 解析成16进制， 判断是否合规。
            if (!(StringUtils.equals(str11, "0D0A") || StringUtils.equals(str11, "0d0a"))) {
                logger.debug("第【2】段数据解析错误， 期望的数据结束位为 0D0A 或 0d0a， 得到的文件头为: " + str11);
                cusIotOriginalHistory.setData(bytesToHexString(bytes));
                cusIotOriginalHistory.setRemark("第【2】段数据解析错误， 期望的数据结束位为 0D0A 或 0d0a， 得到的文件头为: " + str11);
                cusIotOriginalHistoryService.insertCusIotOriginalHistory(cusIotOriginalHistory);
                return;
            }
            logger.debug("第【2】段数据结束位校验通过，解析完成 <--");
            // TODO update database
            cusIoTCurrentService.insertCusIoTCurrent(cusIoTCurrent);
            cusIotVoltageService.insertCusIotVoltage(cusIotVoltage);
            cusIotPowerDataService.insertCusIotPowerData(cusIotPowerDataA);
            cusIotPowerDataService.insertCusIotPowerData(cusIotPowerDataR);
            cusIotPowerDataService.insertCusIotPowerData(cusIotPowerDataF);
            cusIotCurrentHarmonicService.insertCusIotCurrentHarmonic(cusIotCurrentHarmonic);
            cusIotVoltageHarmonicService.insertCusIotVoltageHarmonic(cusIotVoltageHarmonic);
            // 解析成功， 记录原值
            cusIotOriginalHistory.setData(bytesToHexString(bytes));
            cusIotOriginalHistory.setRemark("数据解析成功!");
            cusIotOriginalHistoryService.insertCusIotOriginalHistory(cusIotOriginalHistory);
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

    public static int bytesToIntHigh(byte[] a) {
        int ans = 0;
        for (int i = 0; i < 4; i++) {
            ans <<= 8;
            ans |= (a[i] & 0xff);
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
    public static String bytesToHexString(byte[] bArray) {
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
