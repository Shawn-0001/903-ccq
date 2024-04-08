<template>
    <div class="app-container">
        <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
            <el-descriptions title="谐波电流数据" :column="5" :loading="loading">
                <el-descriptions-item label="Received TimeStamp:" :contentStyle='normal' :labelStyle='labelStyle'>
                    {{ harmonicCurrent.createTime }}
                </el-descriptions-item>
                <el-descriptions-item label="deviceId: " :contentStyle='normal' :labelStyle='labelStyle'>
                    {{ harmonicCurrent.deviceId }}
                </el-descriptions-item>
                <el-descriptions-item label="Device TimeStamp:" :contentStyle='normal' :labelStyle='labelStyle'>
                    {{ harmonicCurrent.timestamp }}
                </el-descriptions-item>
                <el-descriptions-item label="Device IP:" :contentStyle='normal' :labelStyle='labelStyle'>
                    {{ harmonicCurrent.createBy }}
                </el-descriptions-item>
                <el-descriptions-item label="UUID :" :contentStyle='normal' :labelStyle='labelStyle'>
                    {{ harmonicCurrent.uuid }}
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
            <harmonicCurrent :chart-data="chartDataHarmonicCurrent" />
        </el-row>

    </div>
</template>
<script>
import harmonicCurrent from './components/harmonicCurrent'
import { getLatestHarmonicCurrent } from "@/api/IoT/harmonicCurrent";
import { listDeviceList } from "@/api/IoT/deviceList";

export default {
    name: 'echart',
    components: {
        harmonicCurrent,
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
            harmonicCurrent: {},
            loading: true,
            chartDataHarmonicCurrent: {
                harmonicCurrent_A: null,
                harmonicCurrent_B: null,
                harmonicCurrent_C: null,
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
            getLatestHarmonicCurrent(this.deviceId).then(response => {
                // 清空数据
                this.chartDataHarmonicCurrent = {
                    harmonicCurrent_A: null,
                    harmonicCurrent_B: null,
                    harmonicCurrent_C: null,
                }
                this.harmonicCurrent = {}
                // 获取信息
                this.harmonicCurrent =
                {
                    createTime: response.data.createTime,
                    deviceId: response.data.deviceId,
                    timestamp: response.data.timestamp,
                    createBy: response.data.createBy,
                    uuid: response.data.uuid
                }
                this.chartDataHarmonicCurrent = {
                    harmonicCurrent_A: eval(response.data.harmonicA),
                    harmonicCurrent_B: eval(response.data.harmonicB),
                    harmonicCurrent_C: eval(response.data.harmonicC)
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
