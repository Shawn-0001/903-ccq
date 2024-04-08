<template>
    <div class="app-container">
        <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
            <el-descriptions title="电流数据" :column="4" :loading="loading">
                <el-descriptions-item label="Received TimeStamp:" :contentStyle='normal' :labelStyle='labelStyle'>
                    {{ currentData.createTime }}
                </el-descriptions-item>
                <el-descriptions-item label="deviceId: " :contentStyle='normal' :labelStyle='labelStyle'>
                    {{ currentData.deviceId }}
                </el-descriptions-item>
                <el-descriptions-item label="Device TimeStamp:" :contentStyle='normal' :labelStyle='labelStyle'>
                    {{ currentData.timestamp }}
                </el-descriptions-item>
                <el-descriptions-item label="Device IP:" :contentStyle='normal' :labelStyle='labelStyle'>
                    {{ currentData.createBy }}
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
            <currentFFT :chart-data="chartDataCurrent" />
        </el-row>

        <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
            <currentFFT :chart-data="chartDataFFT" />
        </el-row>

    </div>
</template>
<script>
import currentFFT from './components/currentFFT'
import { getLatestCurrent } from "@/api/IoT/current";
import { listDeviceList } from "@/api/IoT/deviceList";

export default {
    name: 'echart',
    components: {
        currentFFT,
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
            currentData: {},
            loading: true,
            chartDataFFT: {
                currentFFT_A: null,
                currentFFT_B: null,
                currentFFT_C: null,
            },
            chartDataCurrent: {
                currentFFT_A: null,
                currentFFT_B: null,
                currentFFT_C: null,
            }
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
                    return { value: `${item.deviceId}`, label: `${item.deviceId}` };
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
            getLatestCurrent(this.deviceId).then(response => {
                // 清空数据
                this.chartDataFFT = {
                    currentFFT_A: null,
                    currentFFT_B: null,
                    currentFFT_C: null,
                }
                this.chartDataCurrent = {
                    currentFFT_A: null,
                    currentFFT_B: null,
                    currentFFT_C: null,
                }
                this.currentData = {}
                // 获取信息
                this.currentData =
                {
                    createTime: response.data.createTime,
                    deviceId: response.data.deviceId,
                    timestamp: response.data.timestamp,
                    createBy: response.data.createBy
                }
                this.chartDataFFT = {
                    currentFFT_A: eval(response.data.currentAFFT),
                    currentFFT_B: eval(response.data.currentBFFT),
                    currentFFT_C: eval(response.data.currentCFFT)
                }
                let currentA = eval(response.data.currentA1).concat(eval(response.data.currentA2))
                let currentB = eval(response.data.currentB1).concat(eval(response.data.currentB2))
                let currentC = eval(response.data.currentC1).concat(eval(response.data.currentC2))
                this.chartDataCurrent = {
                    currentFFT_A: currentA,
                    currentFFT_B: currentB,
                    currentFFT_C: currentC
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
