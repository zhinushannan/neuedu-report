<template>
  <div>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover" class="mgb20" :body-style="{ padding: '0px' }">
          <div class="grid-content grid-con-1">
            <i class="el-icon-user-solid grid-con-icon"></i>
            <div class="grid-cont-right">
              <div class="grid-num">{{ websiteInfo.bookNum }}</div>
              <div>总藏书本数</div>
            </div>
          </div>
        </el-card>
        <el-card shadow="hover" class="mgb20" :body-style="{ padding: '0px' }">
          <div class="grid-content grid-con-3">
            <i class="el-icon-s-goods grid-con-icon"></i>
            <div class="grid-cont-right">
              <div class="grid-num">{{ websiteInfo.borrowCount }}</div>
              <div>总借阅次数</div>
            </div>
          </div>
        </el-card>
        <el-card shadow="hover" class="mgb20" :body-style="{ padding: '0px' }">
          <div class="grid-content grid-con-3">
            <i class="el-icon-s-goods grid-con-icon"></i>
            <div class="grid-cont-right">
              <div class="grid-num">{{ websiteInfo.total }}</div>
              <div>总藏书量</div>
            </div>
          </div>
        </el-card>
        <el-card shadow="hover" class="mgb20" :body-style="{ padding: '0px' }">
          <div class="grid-content grid-con-2">
            <i class="el-icon-message-solid grid-con-icon"></i>
            <div class="grid-cont-right">
              <div class="grid-num">{{ websiteInfo.remain }}</div>
              <div>在馆数量</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <div id="pie" ref="pie" style="width: 100%; height: 100%;"/>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="24" style="padding-right: 5%">
        <div id="bar" ref="bar" style="width: 100%; height: 40vh;"/>
      </el-col>
    </el-row>


  </div>
</template>

<script>
import Schart from "vue-schart";
import {reactive} from "vue";

export default {
  name: "dashboard",
  components: {Schart},
  data() {
    return {
      websiteInfo: JSON.parse(localStorage.getItem("websiteInfo")),
      classify: {}
    }
  },
  methods: {
    loadPie() {
      let _this = this

      let websiteRate = JSON.parse(localStorage.getItem("websiteRate"))
      let rate = []
      for (let i in websiteRate) {
        rate.push({name: i, value: websiteRate[i]})
      }

      let pie = _this.$echarts.init(_this.$refs.pie)
      window.addEventListener("resize", function () {
        pie.resize();
      })
      pie.setOption({
        title: {
          x: 'center',
          y: 'bottom',
          text: '图书馆藏书分类饼状图'
        },
        series: [
          {
            type: "pie",
            data: rate,
            roseType: 'area'
          }
        ],
        tooltip: {
          trigger: 'item',
          axisPointer: {
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          },
          formatter: function (params) {
            let name = _this.classify[params["data"]["name"]]
            let value = params["value"]
            let percent = params["percent"]
            return name + "：" + value + "本<br/>百分比：" + percent + "%"
          }
        },
      })
    },
    loadBar() {
      let _this = this

      let websiteRate = JSON.parse(localStorage.getItem("websiteRate"))
      let xAxisData = []
      let seriesData = []
      for (let i in websiteRate) {
        xAxisData.push(i)
        seriesData.push(websiteRate[i])
      }

      let bar = _this.$echarts.init(_this.$refs.bar)
      window.addEventListener("resize", function () {
        bar.resize();
      })
      bar.setOption({
        title: {
          x: 'center',
          text: '图书馆藏书分类柱状图'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          },
          formatter: function (params) {
            let name = _this.classify[params[0]["axisValue"]]
            let value = params[0]["value"]
            return name + "：" + value + "本<br/>"
          }
        },
        dataZoom: [
          {
            id: 'dataZoomX',
            type: 'slider',
            xAxisIndex: [0],
            filterMode: 'filter'
          },
          {
            id: 'dataZoomY',
            type: 'slider',
            yAxisIndex: [0],
            filterMode: 'empty'
          }
        ],
        xAxis: {
          data: xAxisData
        },
        yAxis: {},
        series: [
          {
            type: "bar",
            data: seriesData
          }
        ]
      })
    }
  },
  mounted() {
    let _this = this
    _this.loadPie()
    _this.loadBar()

    _this.$axios.get("/book/classify/list").then((resp) => {
      let data = resp.data.data
      for (let i in data) {
        _this.classify[data[i]["classifyId"]] = data[i]["classify"]
      }
    })

  }
};
</script>

<style scoped>

.el-row {
  margin-bottom: 20px;
}

.grid-content {
  display: flex;
  align-items: center;
  height: 100px;
}

.grid-cont-right {
  flex: 1;
  text-align: center;
  font-size: 14px;
  color: #999;
}

.grid-num {
  font-size: 30px;
  font-weight: bold;
}

.grid-con-icon {
  font-size: 50px;
  width: 100px;
  height: 100px;
  text-align: center;
  line-height: 100px;
  color: #fff;
}

.grid-con-1 .grid-con-icon {
  background: rgb(45, 140, 240);
}

.grid-con-1 .grid-num {
  color: rgb(45, 140, 240);
}

.grid-con-2 .grid-con-icon {
  background: rgb(100, 213, 114);
}

.grid-con-2 .grid-num {
  color: rgb(45, 140, 240);
}

.grid-con-3 .grid-con-icon {
  background: rgb(242, 94, 67);
}

.grid-con-3 .grid-num {
  color: rgb(242, 94, 67);
}

.user-info {
  display: flex;
  align-items: center;
  padding-bottom: 20px;
  border-bottom: 2px solid #ccc;
  margin-bottom: 20px;
}

.user-avator {
  width: 120px;
  height: 120px;
  border-radius: 50%;
}

.user-info-cont {
  padding-left: 50px;
  flex: 1;
  font-size: 14px;
  color: #999;
}

.user-info-cont div:first-child {
  font-size: 30px;
  color: #222;
}

.user-info-list {
  font-size: 14px;
  color: #999;
  line-height: 25px;
}

.user-info-list span {
  margin-left: 70px;
}

.mgb20 {
  margin-bottom: 20px;
}

.todo-item {
  font-size: 14px;
}

.todo-item-del {
  text-decoration: line-through;
  color: #999;
}

.schart {
  width: 100%;
  height: 300px;
}
</style>
