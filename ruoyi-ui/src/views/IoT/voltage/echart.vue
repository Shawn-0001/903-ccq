<template>
    <div class="app-container">
        <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
            <el-descriptions title="电压数据" :column="4" :loading="loading">
                <el-descriptions-item label="Received TimeStamp:" :contentStyle='normal' :labelStyle='labelStyle'>
                    {{ voltageData.createTime }}
                </el-descriptions-item>
                <el-descriptions-item label="deviceId: " :contentStyle='normal' :labelStyle='labelStyle'>
                    {{ voltageData.deviceId }}
                </el-descriptions-item>
                <el-descriptions-item label="Device TimeStamp:" :contentStyle='normal' :labelStyle='labelStyle'>
                    {{ voltageData.timestamp }}
                </el-descriptions-item>
                <el-descriptions-item label="Device IP:" :contentStyle='normal' :labelStyle='labelStyle'>
                    {{ voltageData.createBy }}
                </el-descriptions-item>

                <template slot="extra">
                    <el-select v-model="deviceId" @change="handleDeviceChange">
                        <el-option v-for="item in deviceList" :key="item.value" :label="item.label" :value="item.value">
                        </el-option>
                    </el-select>
                </template>
            </el-descriptions>
        </el-row>

        <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
            <voltage :chart-data="chartDataVoltage" />
        </el-row>

    </div>
</template>
<script>
import voltage from './components/voltage'
import { getLatestVoltage } from "@/api/IoT/voltage";
import { listDeviceList } from "@/api/IoT/deviceList";

export default {
    name: 'echart',
    components: {
        voltage,
    },
    data() {
        return {
            //内容样式
            normal: { 'text-align': 'left', 'width': '100px', 'font-weight': 'bolder' },
            wider: { 'text-align': 'left', 'width': '150px', 'font-weight': 'bolder' },
            //label样式
            labelStyle: { 'width': 'auto', },
            // 设备列表
            deviceList: [],
            // 当前显示的设备ID
            deviceId: '',
            // 电流详细信息
            voltageData: {},
            loading: true,
            chartDataVoltage: {
                voltage_A: null,
                voltage_B: null,
                voltage_C: null,
            },
        }
    },
    created() {
        this.getDeviceList();
    },
    mounted: function () {
        this.timer = setInterval(this.getData, 10000);//10秒后运行 getData方法
    },
    methods: {
        /** 查询设备列表 */
        getDeviceList() {
            listDeviceList().then(res => {
                this.deviceList = res.rows.map(item => {
                    return { value: `${item.deviceId}`, label: `${item.createBy}` };
                })
                this.deviceId = this.deviceList[0].value
                this.getData();
            })
        },
        /** 选中设备显示图标 */
        handleDeviceChange(value) {
            this.deviceId = value
            this.getData();
        },
        /** 查询电流数据列表 */
        getData() {
            console.log("get data")
            getLatestVoltage(this.deviceId).then(response => {
                // 清空数据
                this.chartDataVoltage = {
                    voltage_A: null,
                    voltage_B: null,
                    voltage_C: null,
                }
                this.voltageData = {}
                // 获取信息
                this.voltageData =
                {
                    createTime: response.data.createTime,
                    deviceId: response.data.deviceId,
                    timestamp: response.data.timestamp,
                    createBy: response.data.createBy
                }
                let currentA = eval(response.data.voltageA1).concat(eval(response.data.voltageA2))
                let currentB = eval(response.data.voltageB1).concat(eval(response.data.voltageB2))
                let currentC = eval(response.data.voltageC1).concat(eval(response.data.voltageC2))
                this.chartDataVoltage = {
                    voltage_A: currentA,
                    voltage_B: currentB,
                    voltage_C: currentC
                }
                this.loading = false;
            });
        },
        //  vue(组件)对象销毁之前，需要把定时器对象销毁
        beforeDestroy() {
            clearInterval(this.timer);
        }
    }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
    padding: 32px;
    background-color: rgb(240, 242, 245);
    position: relative;

    .chart-wrapper {
        background: #fff;
        padding: 16px 16px 0;
        margin-bottom: 32px;
    }
}

@media (max-width:1024px) {
    .chart-wrapper {
        padding: 8px;
    }
}
</style>
