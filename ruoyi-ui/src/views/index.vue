<template>
  <div class="app-container home">
    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:0px;">
      <el-descriptions title="欢迎使用电能监控系统" :column="4" :loading="loading">
        <!-- <el-descriptions-item label="Received TimeStamp:" :contentStyle='normal' :labelStyle='labelStyle'>
          {{ powerDataDetail.createTime }}
        </el-descriptions-item>
        <el-descriptions-item label="deviceId: " :contentStyle='normal' :labelStyle='labelStyle'>
          {{ powerDataDetail.deviceId }}
        </el-descriptions-item>
        <el-descriptions-item label="Device TimeStamp:" :contentStyle='normal' :labelStyle='labelStyle'>
          {{ powerDataDetail.timestamp }}
        </el-descriptions-item>
        <el-descriptions-item label="Device IP:" :contentStyle='normal' :labelStyle='labelStyle'>
          {{ powerDataDetail.createBy }}
        </el-descriptions-item>
        <el-descriptions-item label="Device IP:" :contentStyle='normal' :labelStyle='labelStyle'>
          {{ powerDataDetail.createBy }}
        </el-descriptions-item>
        <el-descriptions-item label="Device IP:" :contentStyle='normal' :labelStyle='labelStyle'>
          {{ powerDataDetail.createBy }}
        </el-descriptions-item>
        <el-descriptions-item label="Device IP:" :contentStyle='normal' :labelStyle='labelStyle'>
          {{ powerDataDetail.createBy }}
        </el-descriptions-item>
        <el-descriptions-item label="Device IP:" :contentStyle='normal' :labelStyle='labelStyle'>
          {{ powerDataDetail.createBy }}
        </el-descriptions-item> -->

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
      <el-table v-loading="loading" :data="powerDataList">
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

    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <currentFFT :chart-data="chartDataCurrent" />
    </el-row>

    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <currentFFT :chart-data="chartDataFFT" />
    </el-row>

    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <voltage :chart-data="chartDataVoltage" />
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
      // // 电流详细信息
      // currentData: {},
      // 最新一条数据UUID
      latestUUID: null,
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
      },
      chartDataVoltage: {
        voltage_A: null,
        voltage_B: null,
        voltage_C: null,
      },
    };
  },
  created() {
    this.getDeviceList();
  },
  mounted: function () {
    this.timer = setInterval(this.getData, 10000);//10秒后运行 getData方法
  },
  methods: {
    handleStopRefresh() {
      clearInterval(this.timer)
    },
    handleStartRefresh() {
      this.timer = setInterval(this.getData, 10000);//10秒后运行 getData方法
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
      console.log("get data")
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
          this.chartDataFFT = {
            currentFFT_A: eval(response.rows[0].currentAFFT),
            currentFFT_B: eval(response.rows[0].currentBFFT),
            currentFFT_C: eval(response.rows[0].currentCFFT)
          }
          let currentA = eval(response.rows[0].currentA1).concat(eval(response.rows[0].currentA2))
          let currentB = eval(response.rows[0].currentB1).concat(eval(response.rows[0].currentB2))
          let currentC = eval(response.rows[0].currentC1).concat(eval(response.rows[0].currentC2))
          this.chartDataCurrent = {
            currentFFT_A: currentA,
            currentFFT_B: currentB,
            currentFFT_C: currentC
          }
        });
        // 获取电压数据
        let voltageParams = {
          UUID: this.latestUUID
        }
        await getVoltageByField(voltageParams).then(response => {
          // 清空数据
          this.chartDataVoltage = {
            voltage_A: null,
            voltage_B: null,
            voltage_C: null,
          }
          // 获取信息
          let currentA = eval(response.rows[0].voltageA1).concat(eval(response.rows[0].voltageA2))
          let currentB = eval(response.rows[0].voltageB1).concat(eval(response.rows[0].voltageB2))
          let currentC = eval(response.rows[0].voltageC1).concat(eval(response.rows[0].voltageC2))
          this.chartDataVoltage = {
            voltage_A: currentA,
            voltage_B: currentB,
            voltage_C: currentC
          }
        });
        // 获取功率因数数据
        let powerDataParams = {
          UUID: this.latestUUID
        }
        getPowerDataByField(powerDataParams).then(res => {
          console.log(res)
          // 获取信息
          this.powerDataDetail =
          {
            createTime: res.rows[0].createTime,
            deviceId: res.rows[0].deviceId,
            timestamp: res.rows[0].timestamp,
            createBy: res.rows[0].createBy
          }
          this.powerDataList = res.rows
        })
      }
      this.loading = false;
    },
    //  vue(组件)对象销毁之前，需要把定时器对象销毁
    beforeDestroy() {
      clearInterval(this.timer);
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
