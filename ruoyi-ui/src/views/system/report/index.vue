<template>
  <div class="app-container home">
    <!-- 第一排：4 个实时指标 -->
    <el-row :gutter="20" class="mb-4">
      <el-col :sm="6" v-for="(item, index) in realTimeIndicators" :key="index">
        <div class="indicator-box">
          <div class="flex justify-between items-center mb-2">
            <h4>{{ item.name }}</h4>
            <el-button
              type="text"
              size="small"
              @click="refreshRealTimeIndicators(index)"
            >
              <i class="el-icon-refresh"></i>
            </el-button>
          </div>
          <p>{{ item.value }}</p>
        </div>
      </el-col>
    </el-row>

    <!-- 第二排：A 库 + B 库与 C 库的每日数量柱状图趋势表 -->
    <el-row :gutter="20" class="mb-4">
      <el-col :sm="12">
        <div class="chart-container">
          <div class="flex justify-between items-center mb-2">
            <h4>A库+B库每日数量趋势</h4>
            <el-button type="text" size="small" @click="refreshABTrend">
              <i class="el-icon-refresh"></i>
            </el-button>
          </div>
          <div id="ab-trend-chart" style="height: 350px;"></div>
        </div>
      </el-col>
      <el-col :sm="12">
        <div class="chart-container">
          <div class="flex justify-between items-center mb-2">
            <h4>C库每日数量趋势</h4>
            <el-button type="text" size="small" @click="refreshCTrend">
              <i class="el-icon-refresh"></i>
            </el-button>
          </div>
          <div id="c-trend-chart" style="height: 350px;"></div>
        </div>
      </el-col>
    </el-row>

    <!-- 第三排：TOP N 排行榜 -->
    <el-row :gutter="20">
      <el-col :sm="12">
        <div class="chart-container">
          <div class="flex justify-between items-center mb-2">
            <h4>库存数量排行</h4>
            <el-button type="text" size="small" @click="refreshInventoryTopN">
              <i class="el-icon-refresh"></i>
            </el-button>
          </div>
          <div id="inventory-topn-chart" style="height: 350px;"></div>
        </div>
      </el-col>
      <el-col :sm="12">
        <div class="chart-container">
          <div class="flex justify-between items-center mb-2">
            <h4>报工批次排行</h4>
            <el-button type="text" size="small" @click="refreshWorkTopN">
              <i class="el-icon-refresh"></i>
            </el-button>
          </div>
          <div id="work-topn-chart" style="height: 350px;"></div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'Index',
  data() {
    return {
      realTimeIndicators: [
        { name: 'A库存总数', value: 100 },
        { name: 'B库存总数', value: 200 },
        { name: 'C库存总数', value: 150 },
        { name: 'D未报工批次', value: 10 }
      ],
      abTrendData: this.generateABTrendData(),
      cTrendData: this.generateCTrendData(),
      inventoryTopNData: this.generateTopNData('inventory'),
      workTopNData: this.generateTopNData('work')
    }
  },
  mounted() {
    this.initABTrendChart()
    this.initCTrendChart()
    this.initInventoryTopNChart()
    this.initWorkTopNChart()
  },
  methods: {
    generateABTrendData() {
      const dates = []
      const aData = []
      const bData = []
      for (let i = 0; i < 30; i++) {
        const date = new Date()
        date.setDate(date.getDate() - 29 + i)
        dates.push(date.toLocaleDateString('zh-CN', { month: 'short', day: 'numeric' }))
        aData.push(Math.floor(Math.random() * 50 + 50))
        bData.push(Math.floor(Math.random() * 50 + 50))
      }
      return { dates, aData, bData }
    },
    generateCTrendData() {
      const dates = []
      const cData = []
      for (let i = 0; i < 30; i++) {
        const date = new Date()
        date.setDate(date.getDate() - 29 + i)
        dates.push(date.toLocaleDateString('zh-CN', { month: 'short', day: 'numeric' }))
        cData.push(Math.floor(Math.random() * 80 + 20))
      }
      return { dates, cData }
    },
    generateTopNData(type) {
      const count = 10
      const data = []
      for (let i = 1; i <= count; i++) {
        // 根据类型生成不同范围的数据
        const baseValue = type === 'inventory' ? 50 : 20
        const randomFactor = type === 'inventory' ? 150 : 80
        data.push({
          id: i,
          count: Math.floor(Math.random() * randomFactor + baseValue),
          type
        })
      }
      // 确保数据按降序排列
      return data.sort((a, b) => b.count - a.count)
    },
    refreshRealTimeIndicators(index) {
      const originalValue = this.realTimeIndicators[index].value
      const newValue = Math.max(0, originalValue + Math.floor(Math.random() * 21) - 10)

      // 添加数字变化动画效果
      let startValue = originalValue
      const duration = 500 // 动画持续时间
      const startTime = Date.now()

      const animate = () => {
        const elapsedTime = Date.now() - startTime
        if (elapsedTime < duration) {
          const progress = elapsedTime / duration
          const currentValue = Math.floor(startValue + (newValue - startValue) * progress)
          this.realTimeIndicators[index].value = currentValue
          requestAnimationFrame(animate)
        } else {
          this.realTimeIndicators[index].value = newValue
        }
      }

      animate()
    },
    refreshABTrend() {
      this.abTrendData = this.generateABTrendData()
      this.initABTrendChart()
    },
    refreshCTrend() {
      this.cTrendData = this.generateCTrendData()
      this.initCTrendChart()
    },
    refreshInventoryTopN() {
      this.inventoryTopNData = this.generateTopNData('inventory')
      this.initInventoryTopNChart()
    },
    refreshWorkTopN() {
      this.workTopNData = this.generateTopNData('work')
      this.initWorkTopNChart()
    },
    initABTrendChart() {
      const chartDom = document.getElementById('ab-trend-chart')
      const myChart = echarts.init(chartDom)
      const option = {
        color: ['#36c', '#69c'],
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            crossStyle: {
              color: '#999'
            }
          }
        },
        legend: {
          data: ['A库', 'B库'],
          right: '10%'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.abTrendData.dates
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: 'A库',
            type: 'bar',
            stack: '总量',
            data: this.abTrendData.aData
          },
          {
            name: 'B库',
            type: 'bar',
            stack: '总量',
            data: this.abTrendData.bData
          }
        ]
      }
      option && myChart.setOption(option)
    },
    initCTrendChart() {
      const chartDom = document.getElementById('c-trend-chart')
      const myChart = echarts.init(chartDom)
      const option = {
        color: ['#3c6'],
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            crossStyle: {
              color: '#999'
            }
          }
        },
        legend: {
          data: ['C库'],
          right: '10%'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.cTrendData.dates
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: 'C库',
            type: 'bar',
            data: this.cTrendData.cData
          }
        ]
      }
      option && myChart.setOption(option)
    },
    initInventoryTopNChart() {
      const chartDom = document.getElementById('inventory-topn-chart')
      const myChart = echarts.init(chartDom)
      const option = {
        color: ['#639'],
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value'
        },
        yAxis: {
          type: 'category',
          // 反转数据顺序，使最大值显示在顶部
          data: this.inventoryTopNData.map(item => `ID ${item.id}`).reverse()
        },
        series: [
          {
            name: '库存数量',
            type: 'bar',
            // 反转数据顺序，与y轴保持一致
            data: this.inventoryTopNData.map(item => item.count).reverse()
          }
        ]
      }
      option && myChart.setOption(option)
    },
    initWorkTopNChart() {
      const chartDom = document.getElementById('work-topn-chart')
      const myChart = echarts.init(chartDom)
      const option = {
        color: ['#f93'],
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value'
        },
        yAxis: {
          type: 'category',
          // 反转数据顺序，使最大值显示在顶部
          data: this.workTopNData.map(item => `ID ${item.id}`).reverse()
        },
        series: [
          {
            name: '报工批次',
            type: 'bar',
            // 反转数据顺序，与y轴保持一致
            data: this.workTopNData.map(item => item.count).reverse()
          }
        ]
      }
      option && myChart.setOption(option)
    }
  }
}
</script>

<style scoped lang="scss">
.home {
  .indicator-box {
    background-color: #fff;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;

    &:hover {
      box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.15);
    }

    h4 {
      margin: 0;
      font-weight: 500;
      color: #909399;
    }

    p {
      margin: 10px 0 0;
      font-size: 28px;
      font-weight: 600;
      color: #303133;
    }
  }

  .chart-container {
    background-color: #fff;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;

    &:hover {
      box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.15);
    }

    h4 {
      margin: 0;
      font-weight: 500;
      color: #303133;
    }
  }

  .el-button {
    padding: 0;
    color: #909399;

    &:hover {
      color: #409eff;
    }
  }

  .mb-4 {
    margin-bottom: 20px;
  }

  .flex {
    display: flex;
  }

  .justify-between {
    justify-content: space-between;
  }

  .items-center {
    align-items: center;
  }
}
</style>
