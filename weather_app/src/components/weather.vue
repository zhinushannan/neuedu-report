<template>

  <div id="weather_app">

    <div class="head">
      <h1>19数据课程报告</h1>
    </div>

    <div class="container">

      <div style="margin: 1.25rem 1.25rem auto 1.25rem">

        <div class="tail_box all_box box_head" style="float:left;">
          <div ref="report" style="height: 40vh; width: 100%;"></div>
        </div>
        <div class="tail_box all_box box_head" style="float:right;">
          <div ref="history" style="height: 40vh; width: 100%;"></div>
        </div>

      </div>

      <div style="margin: 1.25rem 1.25rem auto 1.25rem">
        <div class="tail_box all_box box_head" style="float:left;">
          <div ref="forecast" style="height: 40vh; width: 100%;"></div>
        </div>
        <div class="tail_box all_box box_head" style="float:right;">
          <div ref="temperature" style="height: 40vh; width: 100%;"></div>
        </div>
      </div>

    </div>

  </div>

</template>

<script>
export default {
  name: 'weather',
  data() {
    return {
      report: "",
      history: "",
      temperature: '',
      forecast: '',
    }
  },
  async mounted() {
    this.initDom()
    this.reportChart()
    this.historyChart()
    this.temperatureChart()
    this.forecastChart()
  },
  methods: {
    reportChart() {
      let _this = this
      let chart = this.$echarts.init(_this.report)
      window.addEventListener("resize", function () {
        chart.resize();
      })
      _this.$axios.get("forecast").then((resp) => {
        let option = resp.data
        option["tooltip"] = {
          trigger: 'axis',
          show: true
        }
        option["title"] = {
          text: "一周天气预报",
          x: "center",
          y: "bottom",
          textStyle: {
            fontSize: 14,
            fontWeight: "normal",
            color: "#ffffff"
          }
        }
        option["color"] = ["#FF0000", "#00FF00", "#FF9900", "#00CCFF", "#FFFFCC"]
        chart.setOption(option)
      })
    },
    historyChart() {
      let _this = this
      let chart = this.$echarts.init(_this.history)
      window.addEventListener("resize", function () {
        chart.resize();
      })
      _this.$axios.get("get_history_weather").then((resp) => {
        let option = resp.data
        option["tooltip"] = {
          trigger: 'axis',
          show: true
        }
        let toDay = new Date()
        option["title"] = {
          text: "历史上的今天（" + toDay.getFullYear() + "-" + (toDay.getMonth() + 1).toString().padStart(2, "0") + "-" + (toDay.getDate()) + "）",
          x: "center",
          y: "bottom",
          textStyle: {
            fontSize: 14,
            fontWeight: "normal",
            color: "#ffffff"
          }
        }
        option["color"] = ["#FF0000", "#00FF00", "#FF9900", "#00CCFF", "#FFFFCC"]
        chart.setOption(option)
      })
    },
    temperatureChart() {
      let _this = this
      let chart = this.$echarts.init(_this.temperature)
      window.addEventListener("resize", function () {
        chart.resize();
      })
      // 绘制图表
      _this.$axios.get("get_all_year").then((resp) => {
        let option = resp.data
        option["dataZoom"] = [
          {
            type: "slider",
            realtime: true,
            height: 10,
            start: 0,
            end: 100,
            min: 10
          }
        ]
        option["tooltip"] = {
          trigger: 'axis',
          show: true
        }
        option["title"] = {
          text: "年最高、最低、平均气温",
          x: "center",
          y: "bottom",
          textStyle: {
            fontSize: 14,
            fontWeight: "normal",
            color: "#ffffff"
          }
        }
        option["color"] = ["#FF0000", "#00FF00", "#FF9900", "#00CCFF", "#FFFFCC"]
        chart.setOption(option)
      })
    },
    forecastChart() {
      let _this = this
      let chart = this.$echarts.init(_this.forecast)
      window.addEventListener("resize", function () {
        chart.resize();
      })
      // 绘制图表
      _this.$axios.get("get_all_forecast").then((resp) => {
        let option = resp.data
        option["tooltip"] = {
          trigger: 'axis',
          show: true
        }
        option["title"] = {
          text: "天气走向曲线",
          x: "center",
          y: "bottom",
          textStyle: {
            fontSize: 14,
            fontWeight: "normal",
            color: "#ffffff"
          }
        }
        option["color"] = ["#FF0000", "#00FF00", "#FF9900", "#00CCFF"]
        chart.setOption(option)
      })
    },
    initDom() {
      this.report = this.$refs.report
      this.history = this.$refs.history
      this.temperature = this.$refs.temperature
      this.forecast = this.$refs.forecast
    },
  }
}
</script>

<style scoped>

#weather_app {
  background: #000d4a url("../assets/background.jpg") center top;
  background-size: cover;
  color: #666;
  font-family: "微软雅黑", serif;
  height: 100vh;
  font-size: 1rem;
}

.head {
  height: 8vh;
  width: 100%;
  background: url("../assets/head_bg.png") no-repeat center center;
  background-size: 100% 100%;
}

h1 {
  text-align: center;
  color: rgba(255, 255, 255);
  font-weight: bold;
  line-height: 8vh;
}

.top_box {
  height: 50%;
  width: 96%;
  margin: 1.25rem auto auto;
}

.tail_box {
  height: 50%;
  width: 49%;
  float: left;
}

.all_box {
  border: .0625rem solid rgba(25, 186, 139, .17);
  padding: 0rem .2rem .4rem .15rem;
  background: rgba(255, 255, 255, .04) url("../assets/line.png");
  background-size: 100% auto;
  position: relative;
  margin-bottom: .15rem;
  z-index: 10;
}

.box_head::after {
  position: absolute;
  width: 0.5rem;
  height: 0.5rem;
  content: "";
  border-top: 0.125rem solid #02a6b5;
  border-right: 0.125rem solid #02a6b5;
  top: 0;
  right: 0;
}

.box_foot::before {
  position: absolute;
  width: 0.5rem;
  height: 0.5rem;
  content: "";
  border-bottom: 0.125rem solid #02a6b5;
  border-left: 0.125rem solid #02a6b5;
  bottom: 0;
  left: 0;
}

.title {
  height: 4.3vh;
  font-size: 1rem;
  color: white;
  text-align: center;
  line-height: 4.3vh;
}

</style>
