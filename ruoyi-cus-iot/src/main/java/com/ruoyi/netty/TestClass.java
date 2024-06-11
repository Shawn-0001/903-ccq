package com.ruoyi.netty;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.iot.domain.CusIotVoltage;
import com.ruoyi.iot.service.*;
import org.jtransforms.fft.FloatFFT_1D;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestClass {
    @Autowired
    private ICusIoTCurrentService cusIoTCurrentService;
    @Autowired
    private ICusIotVoltageService cusIotVoltageService;
    @Autowired
    private ICusIotPowerDataService cusIotPowerDataService;
    @Autowired
    private ICusIotVoltageHarmonicService cusIotVoltageHarmonicService;
    @Autowired
    private ICusIotCurrentHarmonicService cusIotCurrentHarmonicService;

    @Autowired
    private RedisCache redisCache;


    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey) {
        return CacheConstants.SYS_CONFIG_KEY + configKey;
    }

    @Test
    public void testConfigKey() {
        String configValue = Convert.toStr(redisCache.getCacheObject(getCacheKey("cus.current.multiple")));
        System.out.println(configValue);
    }

    @Test
    public void testByteLength() {
        // 初始化, 生成UUID, e.g. ef21517ac0f94db4b464f7bf61f6b656
        String currentUUID = IdUtils.fastSimpleUUID();
        System.out.println(currentUUID);
        System.out.println(currentUUID.length());
        // UUID 转成byte， 添加到byte头部
        byte[] UUIDBytes = currentUUID.getBytes(StandardCharsets.UTF_8);
        System.out.println(Arrays.toString(UUIDBytes));
        System.out.println(UUIDBytes.length);
    }

    @Test
    public void testUUID() {
        String fastUUID = IdUtils.fastUUID();
        String fastSimpleUUID = IdUtils.fastSimpleUUID();
        String randomUUID = IdUtils.randomUUID();
        String simpleUUID = IdUtils.simpleUUID();

        System.out.println(fastUUID);
        System.out.println(fastSimpleUUID);
        System.out.println(randomUUID);
        System.out.println(simpleUUID);
    }

    @Test
    public void testData() {
        // get the latest 10000 lines
        CusIotVoltage voltage = new CusIotVoltage();
        Map<String, Object> params = new HashMap<>();
        params.put("beginCreateTime", "2024-03-29 08:00:00");
        params.put("endCreateTime", "2024-03-29 17:00:00");

        List<CusIotVoltage> voltageList = cusIotVoltageService.selectCusIotVoltageList(voltage);


    }

    @Test
    public void test1() {
        String str = "683F007C484E303130313031303130313031353500002E00130004001700340057005C00F201510663079705B203F401570136028B02E101400171018501A301BB01CC01E301F901080219022E028F022206DA081409430828060704D1037403870275027F0291029C02950290028B028402750263024B0234021D020A02F201DE01C101A40181015E013A01FE00A40049000100E2FFE4FF02000F00F3FFD7FFB8FFAEFF03FEFFF99AF8C6FAACFC02FEE1FEC9FD16FD5DFECDFEA5FE83FE56FE4FFE3BFE24FE16FEFFFDE8FDD6FD82FD83FA0AF7B5F685F811FAD1FBD7FCD9FC88FD87FD7CFD72FD61FD64FD6CFD71FD7CFD86FD97FDA9FDBDFDD3FDE7FD00FE16FE35FE51FE72FE95FEB8FEF0FE46FFA1FFEFFF11000C00EEFFE7FFFDFF17003A004200C30151051507A6050D03DE017001E6019302D1011501500168018501A401B501CE01E301F60109022002BC02CB052509D7095B07D105D9043903220313036002830290029A02920293028E0288027D026C025502420228021302FC01E601CB01B00192016B0144010801B10059001000EBFFF0FF130022000300E7FFC1FFB4FF31FD1DFACEF8F1F951FD95FEF6FDCBFDA5FD9CFDE0FEA9FE8FFE60FE58FE47FE30FE1FFE0BFEF7FDE5FD52FDB7F936F7C2F66CF881FAF9FBEDFC3BFD95FD91FD80FD75FD66FD6BFD75FD7AFD80FD8FFDA2FDB9FDCEFDE2FDF8FD0CFE24FE42FE62FE82FEA6FECEFE09FF60FFBAFF010023008CFDEAFD27FE13FE03FEEFFDF6FD41FC08F82AF707F90BFBD2FC63FDA2FC66FC2BFDD4FDC1FDBCFDB3FDB5FDB6FDB7FDBDFDC4FDCBFDD6FDDFFDE5FDF0FDFCFD0DFE25FE3DFE5AFE79FE98FEBAFED7FEF2FE11FF1CFF32FF4FFF67FF83FFA7FFC2FF55007F03A305A20493020A02D001C801310228010101880103026902930263020202D101E001F40110020402BE03B307D7088E06A2043F035B026A03FC0391021E0236023A0249024C02480246023F02390234022A02200214020902FD01ED01D601BF01A10188016A014B012D010901E900E100D100BA009C00820061003F00E0FF9DFD14FB49FBB8FD1BFEFDFDBAFE3FFEB7FE0FFF7DFEFCFD9BFD7BFDA0FD00FE33FE1FFE0DFEF9FDF8FD5DFCEEF84FF7E2F891FBBEFC33FDD4FC3FFC1DFDDCFDC0FDB9FDB0FDACFDA6FDA6FDADFDB2FDBAFDC3FDCAFDD4FDDBFDE5FDF3FD09FE21FE3FFE5DFE7DFE9CFEB8FED9FEF8FE01FF18FF2FFF48FF6AFF8FFFA4FFB200390430057904A4029201C401CC01C2010A01F2007F01F9015E0281024E02EB01BD01CE01E3010702090299049007B6085D07EF03C002480359037D0353030A0235023202450241023E023A023A02300227022002180210020502F601E301CE01B6019C017C015D01410121010001E400DA00C700AE008F007500570030007FFFE7FB33FBE7FBD0FC6BFEB2FED7FD28FE28FFE8FE70FEF0FD8FFD71FD57021702ED01E501DB01CE01C201B101A30193018101670152013F0129011301FA00E300C900B6009D0088007200550043002A000D00F9FF6CFFA2FB59F928F9CAF9ABFBA0FDADFDF7FDD6FEDAFEB2FE92FE76FE78FE6BFE56FE3CFE2EFE20FE11FE83FD5BFA63F88CF9A6FB38FC85FCB1FC6EFC9AFDF1FDC5FD9AFD81FD83FDA5FDE5FD14FE1AFE24FE31FE3EFE52FE61FE73FE84FE9CFEB3FECAFEDDFEF1FE08FF1DFF3AFF53FF6DFF77FF8BFFA2FFC1FFD7FFEFFF0E008400B403EA060A07820526048D02AA01C10121012A014D01790199018D019801AB01BB01CF01E201F00147029C04F90695062604B403B603DF023E0389021802520271028E028E0263022802FE01F501EA01E001D501C801BC01AD019C01860170015A0143012C0118010301EB00D200BC00AA0093007900640048002B0015004BFF1AFCF7F88BF8CFFAEEFBF3FC6FFE3FFE73FE0DFFCAFEAAFE91FE94FE85FE6CFE53FE43FE2DFE23FE10FD9AF9D0F89FF98BFBA6FC77FCA0FCCDFCA7FDF7FDBEFD98FD83FD86FDB0FDEDFD15FE1AFE24FE30FE3EFE4BFE5AFE72FE84FE9EFEB0FEC3FED7FEEDFE05FF1BFF32FF4BFF67FF76FF8CFFA1FFBCFFD4FFEFFF0700CD007804B4060F079C05B903770294016B011F0129014F017B0195018B019601B001BF01D101E701EE01A5023E06BB06FF0500054303F702BA033303130227024602690286028502101D851AAE17E314E9111F0F3C0C49092906FA02A6FF0AFC9BF862F5E7F1ECEEDBEB15E9ACE662E417E2B4DF33DDB3DA37D8A6D578D32DD15DCFF3CD6BCCF0CA5EC935C73DC585C3B5C149C0A4BF17BFA5BE96BED5BE42BF2EC0FFC05FC11CC2C2C27FC3E0C439C64AC7CCC894CA5ACC58CE5AD01FD256D457D671D8F2DAB9DDB6E016E314E62BE92EEC38EF62F27EF586F8ABFB24FF4B028E05F008FA0B180F3E12FE147C17F0193B1C9B1E202196231126A128ED2A1C2D0A2F8D30E8315E330E350A37F738B73A903CE13D943E2C3F8F3FAB3F5D3FD13EE43D473D773C0D3C693B703A3839EF37B8362E358833B431992F9A2DF62B8529A1277125C422D01FDF1C631A7517C114C311F40E1B0C1909F705D7025CFFDEFB5BF81CF5AEF1A8EEA9EBF7E88DE648E4F9E190DF1BDD89DA0CD87DD54AD30CD149CFDACD5DCCD8CA4DC916C731C56FC39BC140C08FBF0BBF94BE90BEDEBE4EBF44C00FC163C129C2C4C28EC3FDC442C662C7DDC8B2CA71CC71CE7ED044D289D477D697D817DBDEDDDAE028E33AE642E951EC62EF8DF2B1F5C2F8DBFB4FFF6F02B2051009210C3C0F5A1223159D17161A5A1CBE1E3F21B3232426AE280C2B2E2D152FA530F931713330352737FB38DE3AA33CE53D9B3E343F933FA83F5C3FCA3EE93D423D6C3C183C663B4E3A2139EE379836173578339C316C2F8D2DDA2B622986275B25A822BB1F2120AA22EC246727BA293E2CAE2E8E302932D83389352337E838BE3A213CAA3DCE3E843F21407A407D4026409E3FE03E3D3E873D0A3D763CAE3B533AF9387437D33582336831832F7F2DEF2B632A94286B26F7232021FB1DE51A8717CD138D10650D4D0AA7071A0522024BFF34FCBBF84DF5D9F152EE20EB19E86FE547E366E18FDFE0DCA0DA30D886D5DFD259D079CECDCC1ECBA9C9ECC73BC6C8C40EC3B5C198C0CBBF2ABFE0BECCBE1DBFB4BF6AC025C1E2C181C200C3E9C34EC58EC605C8D5C9EDCB04CE0AD0FED186D332D507D72ED9B8DB8EDE91E19CE426E867EBEDEE1BF2E6F4BDF77BFA3CFD2B007003D5062E0AB60D561156148117381A591C451E4020CE2205258C27D929572CD62EA0303832FD3391354A370639D43A3D3CBC3DE93E9E3F29408240744011408F3FCA3E2C3E823D093D7E3C9F3B423ADE385B37C6355D334E31692F5E2DDB2B542A7A284726CE23F420CC1DB81A531793135E10360D210A8E07EE04080224FF06FC86F819F59EF119EEE7EAD6E744E532E354E188DFD0DC9FDA1DD871D5C2D233D06BCEAECC05CB9CC9D5C72AC6B6C404C3A3C187C0B9BF18BFD6BEC5BE1FBFB7BF6FC033C1F4C18FC206C3F5C357C59AC61AC8E8C910CC18CE21D01FD29ED34FD51FD74ED9DADBB3DEB6E1CDE452E88CEB23EF40F20BF5E5F799FA51FD5300970309075A0AEC0D83117614A417521A751C5E1E11BF2AC0D9C086C1D1C141C2B6C2C9C354C5F5C6C6C899CA8ACC8BCE0AD0BBD101D3E1D40BD78DD95DDC7BDFC6E21CE664E951EC30EFCCF175F415F7F1F923FDCB003C040608C50BFB0E0812DA140E17DA18AA1AB21C081F04220925D527D72A782D502FBC301F32113377341936BC37A139753B073D0B3EAF3EF73E153FEC3E833E4A3DA23C1C3CAB3B413BEB3AA53912386A36DB34E1320331612F952D0E2CD02ACA289A2637246121591E2A1BC6177F148111780EAF0BFE08560668034000E0FC57F99DF5FEF1BCEE8DEBC1E87FE6A9E4E5E2DDE075DE84DBB8D880D5D5D242D01ACED5CCAACB6DCA13C99AC7E0C5DFC32BC2B8C07EBFFABEB5BE8ABEA9BE21BF3DC0D7C08DC1CFC140C2C6C2D2C363C51BC7CDC8C0CAABCC9BCE28D0CED115D308D524D7B5D987DCA1DFFFE24AE687E97BEC55EFF9F19CF437F719FA4FFD01016C044A08000C290F40120B153017EF18C71AC91C2A1F2E223525FC27082B9A2D5E2FE1302032193389342C36CB37BA398F3B143D153EB63EF83E133FEB3E7D3E2D3D9D3C153C9D3B3B3BD33A8339FC374636B834CD32EB30492F8D2D042CBA2AB0287C26142438212D1EF81A98174F145111580E880BDC083A0646031E00B8FC29F95DF5D0F18FEE60EB9EE866E692E4D0E2C3E059DE6ADB95D858D5BCD220D00FCED1CC97CB58CA0DC984C7CFC5C9C313C2A2C06FBFF4BEB1BE8BBEAEBE0D0A683F007C487630313031303130313031303135350000B2E6FFFF27E7FFFF43EAFFFF1BB8FFFF4F100000440C0000980E00002C2B00003FFDFFFFF5FCFFFF4DFDFFFF2CFDFFFF0C002E0006005E000E0072000D001300090039000800160001001200000003000000070000000A00000006000000020001000400010001000000030000000200010004000000020000000400050001000000020001000100010002000100010000000100010002000100010001000200010002000400010001000000080038000B004E0008006C00070027000A004B0003000D00020017000100060000000300000009000100020000000400000004000100020000000500000002000100040000000300010004000500010000000100010001000100010001000100010001000100010001000300000001000100020004000000010001000600250008004B0007006100060024000300650005000E000200160001000E0001000C0001000A0001000500010006000000030001000300000002000000020000000300000001000000050004000200010003000100000001000200010001000100010001000200010002000100020001000300040001000100010092007A048200BC03C50056039A004A02450010039000FB013A00A902BA0063028900C10132006801C3009802BE0086014600A2019E00AF01730041011900F40026002B010E00C700410026014C00D5004F0050006900DD005200A0005A00150050006700360079002A000E002D00330013001D0021001A0016003300DA001804770090033E009E0268003D024900C2021B00520250001D028000CA016E004001750022019200E2019400B4019A000A017E00BF005500700043009F0018008C0013004B002A002E004A00A1005100A00071004F0071008E00510046005D00670039007D00200027001D001900130043001400640020000E00C60043024F00F802B40097036700B4029700150387008503AC0058024900A60281007502500098025400DF01570099026400CB012500A5012F0098012A00B3011A0000011E005801250047011400B3000D00BF000B00B8000D00590015005B000B00A3002000380016002D000B003B0010002F001D0049001A003B000D0A";
        System.out.println(str.length());
//        System.out.println(str.getBytes().length);
    }

    /*
     * 测试傅里叶变换
     *
     * */

    @Test
    public void testFFT() {

        float[] data = {9f, 192f, 366f, 533f, 652f, 715f, 803f, 881f, 988f, 1047f, 1138f, 1169f, 1279f, 1368f, 1403f, 1526f, 1616f, 1652f, 1769f, 1821f, 1844f, 1894f, 1917f, 1916f, 1962f, 1964f, 2003f, 2018f, 2084f, 2099f, 2188f, 2239f, 2288f, 2334f, 2395f, 2456f, 2509f, 2589f, 2643f, 2716f, 2793f, 2845f, 2869f, 2930f, 2964f, 3042f, 3059f, 3141f, 3158f, 3198f, 3203f, 3163f, 3118f, 3054f, 2998f, 2944f, 2842f, 2697f, 2589f, 2458f, 2311f, 2103f, 1885f, 1658f, 1427f, 1174f, 908f, 639f, 421f, 200f, -9f, -207f, -380f, -505f, -617f, -738f, -808f, -887f, -945f, -1040f, -1113f, -1197f, -1276f, -1355f, -1458f, -1545f, -1600f, -1673f, -1776f, -1831f, -1850f, -1895f, -1913f, -1935f, -1971f, -1977f, -2018f, -2046f, -2082f, -2157f, -2197f, -2229f, -2295f, -2354f, -2420f, -2487f, -2518f, -2575f, -2612f, -2710f, -2776f, -2867f, -2858f, -2910f, -2989f, -3026f, -3069f, -3083f, -3153f, -3168f, -3175f, -3136f, -3067f, -3049f, -2972f, -2895f, -2818f, -2700f, -2563f, -2425f, -2256f, -2062f, -1884f, -1652f, -1384f, -1138f, -869f, -609f, -344f, -129};
        FloatFFT_1D dft = new FloatFFT_1D(140);
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
        for (int i = 0; i < realPart.length; i++) {
            float x = realPart[i];
            BigDecimal bx = new BigDecimal(String.valueOf(x));
            double dx = bx.doubleValue();
            float y = imaginaryPart[i];
            BigDecimal by = new BigDecimal(String.valueOf(y));
            double dy = by.doubleValue();
            cur_fft[i] = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
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
