<template>
  <div :class="className" :style="{ height: height, width: width }" />
</template>

<script>
import * as echarts from 'echarts'
import resize from './mixins/resize'

export default {
  name: "harmonicVoltage",
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '350px'
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    chartData: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      chart: null
    }
  },
  watch: {
    chartData: {
      deep: true,
      handler(val) {
        this.setOptions(val)
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el)
      this.setOptions(this.chartData)
    },
    setOptions({ harmonicVoltage_A, harmonicVoltage_B, harmonicVoltage_C } = {}) {
      this.chart.setOption({
        legend: {
          data: ['harmonicVoltage_A', 'harmonicVoltage_B', 'harmonicVoltage_C']
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          // data: Array.from(new Array(63 + 1).keys()).slice(0),
          axisTick: {
            show: false
          }
        },
        grid: {
          left: 10,
          right: 10,
          bottom: 20,
          top: 30,
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          padding: [5, 10]
        },
        yAxis: {
          axisTick: {
            show: false
          },
          type: 'value',

        },

        series: [{
          name: 'harmonicVoltage_A',
          type: 'line',
          data: harmonicVoltage_A,
          showSymbol: false,
          animationDuration: 1000,
          animationEasing: 'cubicInOut',
          itemStyle: {
            normal: {
              color: '#FF005A',
              lineStyle: {
                color: '#FF005A',
                width: 2
              },
            }
          },
          // 显示最大值
          markPoint: {
            data: [
              { type: 'max', name: '最大值' },
              { type: 'min', name: '最小值' }
            ]
          },
        },
        {
          name: 'harmonicVoltage_B',
          type: 'line',
          data: harmonicVoltage_B,
          showSymbol: false,
          animationDuration: 1000,
          animationEasing: 'quadraticOut',
          itemStyle: {
            normal: {
              color: '#3888fa',
              lineStyle: {
                color: '#3888fa',
                width: 2
              },
              areaStyle: {
                color: '#f3f8ff'
              }
            }
          },
          // 显示最大值
          // markPoint: {
          //   data: [
          //     { type: 'max', name: '最大值' },
          //     { type: 'min', name: '最小值' }
          //   ]
          // },
        },
        {
          name: 'harmonicVoltage_C',
          type: 'line',
          data: harmonicVoltage_C,
          showSymbol: false,
          animationDuration: 1000,
          animationEasing: 'quadraticOut',
          itemStyle: {
            normal: {
              color: '#40c9c6',
              lineStyle: {
                color: '#40c9c6',
                width: 2
              },
              areaStyle: {
                color: '#f3f8ff'
              }
            }
          },
          // 显示最大值
          // markPoint: {
          //   data: [
          //     { type: 'max', name: '最大值' },
          //     { type: 'min', name: '最小值' }
          //   ]
          // },
        }
        ]
      })
    }
  }
}
</script>
