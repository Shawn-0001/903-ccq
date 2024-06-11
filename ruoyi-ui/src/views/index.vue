<template>
  <div class="app-container home">
    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:0px;">
      <el-descriptions title="" :column="4">
        <template slot="title">
          <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:0px;">
            <el-col>
              <div>
                <span style="font-size: large;">欢迎使用电能监控系统</span>
                <span style="margin-left: 100px; font-size: small;">系统时间： </span>
                <span style="font-size: small; color:#4169E1">{{ currentTime }}</span>
                <span style="margin-left: 20px; font-size: small;">当前数据接收时间：</span>
                <span style="font-size: small; color:#4169E1">{{ currentDataTime }}</span>
                <span style="margin-left: 100px; font-size: small;">自动刷新开关 ： </span>
                <span style="font-size: small; color:#4169E1">{{ isTimerRunning }}</span>
              </div>
            </el-col>
          </el-row>
        </template>
        <template slot="extra">
          <el-button type="warning" size="small" @click="handleStopRefresh">停止自动刷新</el-button>
          <el-button type="success" size="small" @click="handleStartRefresh">启动刷新（默认自动启动）</el-button>
          <el-select style="margin-left: 10px;" v-model="deviceId" @change="handleDeviceChange">
            <el-option v-for="item in deviceList" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </template>
      </el-descriptions>
    </el-row>

    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <el-table :data="powerDataList">
        <el-table-column label="设备ID" align="center" prop="deviceId" />
        <el-table-column label="功率数据" align="center" prop="type">
          <template slot-scope="{}" slot="header">
            <span>功率数据</span>
            <el-tooltip class="item" effect="dark" placement="top">
              <i class="el-icon-question" style="font-size: 14px; vertical-align: middle;"></i>
              <div slot="content">
                <p>P ：有功功率</p>
                <p>Q ：无功功率</p>
                <p>C ：consinΦ,功率因素</p>
              </div>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column label="A相" align="center" prop="phaseA" />
        <el-table-column label="B相" align="center" prop="phaseB" />
        <el-table-column label="C相" align="center" prop="phaseC" />
        <el-table-column label="总计" align="center" prop="total" />
        <el-table-column label="设备IP地址" align="center" prop="createBy" />
        <el-table-column label="设备上传数据时间" align="center" prop="createTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="每条数据的UUID" align="center" prop="uuid" show-overflow-tooltip min-width="150px" />
        <el-table-column label="上传的时间戳" align="center" prop="timestamp" />
      </el-table>
    </el-row>

    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:0px;">
      <el-col :xs="24" :sm="24" :lg="12">
        <div class="chart-wrapper">
          <voltage :chart-data="chartDataVoltage" />
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="12">
        <div class="chart-wrapper">
          <voltage :chart-data="chartDataVoltageFFT" />
        </div>
      </el-col>
    </el-row>
    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:0px;">
      <el-col :xs="24" :sm="24" :lg="12">
        <div class="chart-wrapper">
          <currentFFT :chart-data="chartDataCurrent" />
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="12">
        <div class="chart-wrapper">
          <currentFFT :chart-data="chartDataFFT" />
        </div>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import currentFFT from '@/views/IoT/current/components/currentFFT'
import voltage from './IoT/voltage/components/voltage.vue';
import { getCurrentByField } from "@/api/IoT/current";
import { getVoltageByField } from "@/api/IoT/voltage";
import { listDeviceList, listDeviceByField } from "@/api/IoT/deviceList";
import { getPowerDataByField } from "@/api/IoT/powerData"

export default {
  name: "Index",
  components: {
    currentFFT,
    voltage
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
      // 功率因数详细信息
      powerDataDetail: {},
      powerDataList: [],
      // 显示系统时间
      currentTime: '',
      // 显示当前数据接收时间
      currentDataTime: '',
      // 判断定时器是否运行
      isTimerRunning: '开',
      // 最新一条数据UUID
      latestUUID: null,
      chartDataFFT: {
        currentFFT_A: null,
        currentFFT_B: null,
        currentFFT_C: null,
        chartTitile: '电流谐波',
        legend: ['A相谐波', 'B相谐波', 'C相谐波']
      },
      chartDataVoltageFFT: {
        voltageFFT_A: null,
        voltageFFT_B: null,
        voltageFFT_C: null,
        chartTitile: '电压谐波',
        legend: ['A相谐波', 'B相谐波', 'C相谐波']
      },
      chartDataCurrent: {
        currentFFT_A: null,
        currentFFT_B: null,
        currentFFT_C: null,
        chartTitile: '电流',
        legend: ['A相', 'B相', 'C相']
      },
      chartDataVoltage: {
        voltage_A: null,
        voltage_B: null,
        voltage_C: null,
        chartTitile: '电压',
        legend: ['A相', 'B相', 'C相']
      },
    };
  },
  created() {
    this.getDeviceList();
  },
  mounted: function () {
    this.currentTimer = setInterval(this.getCurrentTime, 1000);//每秒运行一次
    this.timer = setInterval(this.getData, 10000);//10秒后运行 getData方法
  },
  methods: {
    handleStopRefresh() {
      clearInterval(this.timer)
      // 设置刷新状态
      this.isTimerRunning = '关'
    },
    handleStartRefresh() {
      if (this.timer) {
        clearInterval(this.timer)
      }
      this.getData()
      this.timer = setInterval(this.getData, 10000);//10秒后运行 getData方法
      // 设置刷新状态
      this.isTimerRunning = '开'
    },
    getCurrentTime() {
      this.currentTime = this.parseTime(new Date())
    },
    /** 查询设备列表 */
    getDeviceList() {
      listDeviceList().then(res => {
        this.deviceList = res.rows.map(item => {
          return { value: `${item.deviceId}`, label: `${item.deviceId}`, UUID: `${item.latestUUID}` };
        })
        this.deviceId = this.deviceList[0].value
        this.getData()
      })
    },
    /** 选中设备显示图标 */
    handleDeviceChange(value) {
      this.deviceId = value
      this.getData();
    },
    /** 查询数据列表 */
    async getData() {
      console.log("get data at:" + this.currentTime)
      // 获取device ID对应的最新UUID
      let deviceParams = {
        deviceId: this.deviceId
      }
      await listDeviceByField(deviceParams).then(res => {
        this.latestUUID = res.rows[0].latestUUID
      })
      if (this.latestUUID != null && this.latestUUID != '') {
        // 清空详细信息数据
        this.powerDataDetail = {}
        // 获取电流数据
        let currentParams = {
          UUID: this.latestUUID
        }
        // 同步请求
        await getCurrentByField(currentParams).then(response => {
          if (response.rows !== '') {
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
            // 获取信息转数组
            let currentAFFT_28 = JSON.parse(response.rows[0].currentAFFT).slice(0, 28)
            let currentBFFT_28 = JSON.parse(response.rows[0].currentBFFT).slice(0, 28)
            let currentCFFT_28 = JSON.parse(response.rows[0].currentCFFT).slice(0, 28)

            this.chartDataFFT = {
              currentFFT_A: currentAFFT_28,
              currentFFT_B: currentBFFT_28,
              currentFFT_C: currentCFFT_28,
              chartTitile: '电流谐波',
              legend: ['A相谐波', 'B相谐波', 'C相谐波']
            }

            let currentA = eval(response.rows[0].currentA1).concat(eval(response.rows[0].currentA2))
            let currentB = eval(response.rows[0].currentB1).concat(eval(response.rows[0].currentB2))
            let currentC = eval(response.rows[0].currentC1).concat(eval(response.rows[0].currentC2))
            this.chartDataCurrent = {
              currentFFT_A: currentA,
              currentFFT_B: currentB,
              currentFFT_C: currentC,
              chartTitile: '电流',
              legend: ['A相', 'B相', 'C相']
            }
          } else {
            this.$modal.msgWarning("未找到最新数据! ");
          }
        });
        // 获取电压数据
        let voltageParams = {
          UUID: this.latestUUID
        }
        await getVoltageByField(voltageParams).then(response => {
          if (response.rows !== '') {
            // 清空数据
            this.chartDataVoltage = {
              voltage_A: null,
              voltage_B: null,
              voltage_C: null,
              chartTitile: '电压',
              legend: ['A相', 'B相', 'C相']
            }
            this.chartDataVoltageFFT = {
              voltage_A: null,
              voltage_B: null,
              voltage_C: null,
              chartTitile: '电压谐波',
              legend: ['A相谐波', 'B相谐波', 'C相谐波']
            }
            // 获取信息
            let voltageA = eval(response.rows[0].voltageA1).concat(eval(response.rows[0].voltageA2))
            let voltageB = eval(response.rows[0].voltageB1).concat(eval(response.rows[0].voltageB2))
            let voltageC = eval(response.rows[0].voltageC1).concat(eval(response.rows[0].voltageC2))
            this.chartDataVoltage = {
              voltage_A: voltageA,
              voltage_B: voltageB,
              voltage_C: voltageC,
              chartTitile: '电压',
              legend: ['A相', 'B相', 'C相']
            }

            let voltageAFFT_28 = JSON.parse(response.rows[0].voltageAFFT).slice(0, 28)
            let voltageBFFT_28 = JSON.parse(response.rows[0].voltageBFFT).slice(0, 28)
            let voltageCFFT_28 = JSON.parse(response.rows[0].voltageCFFT).slice(0, 28)


            this.chartDataVoltageFFT = {
              voltage_A: voltageAFFT_28,
              voltage_B: voltageBFFT_28,
              voltage_C: voltageCFFT_28,
              chartTitile: '电压谐波',
              legend: ['A相谐波', 'B相谐波', 'C相谐波']
            }
          } else {
            this.$modal.msgWarning("未找到最新数据! ");
          }
        });
        // 获取功率因数数据
        let powerDataParams = {
          UUID: this.latestUUID
        }
        getPowerDataByField(powerDataParams).then(res => {
          // 获取信息
          this.powerDataDetail =
          {
            createTime: res.rows[0].createTime,
            deviceId: res.rows[0].deviceId,
            timestamp: res.rows[0].timestamp,
            createBy: res.rows[0].createBy
          }
          this.powerDataList = res.rows
          this.currentDataTime = res.rows[0].createTime
        })
      }
    },
    //  vue(组件)对象销毁之前，需要把定时器对象销毁
    beforeDestroy() {
      clearInterval(this.timer);
      clearInterval(this.currentTimer);
    }
  }
};
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }

  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }

  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans",
  "Helvetica Neue",
  Helvetica,
  Arial,
  sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>
