// 测试用 api 实例
let express = require('express');
let router = express.Router();
let conn = require("../config/mysql").conn
let $weather_sql = require('../sql/weather');


// 增加用户接口
// GET 请求
router.get('/get_history_weather', (req, res) => {
  let query = $weather_sql.query_by_date;

  let dayInYear = new Date()
  dayInYear = dayInYear.getDate().toString().padStart(2, "0") + "/" + (dayInYear.getMonth() + 1).toString().padStart(2, "0") + "/%"

  conn.query(query, [dayInYear], (err, result) => {

    let year = []
    let maxTemperature = []
    let minTemperature = []
    let avgTemperature = []
    let precipitation = []

    for (let item in result) {
      let data = result[item]
      if (data["maxTemperature"] === 0 || data["minTemperature"] === 0 || data["avgTemperature"] === 0) {
        continue
      }
      year.push(data["date"].split("/")[2])
      maxTemperature.push(data["maxTemperature"])
      minTemperature.push(data["minTemperature"])
      avgTemperature.push(data["avgTemperature"])
      precipitation.push(data["precipitation"])
    }

    let option = {
      xAxis: {
        data: year,
        axisLabel: {
          textStyle: {
            color: "#ffffff"
          }
        }
      },
      yAxis: [
        {
          name: "温度(°C)",
          splitNumber: 3,
          axisLabel: {
            textStyle: {
              color: "#ffffff"
            }
          }
        },
        {
          name: "降水量(mm)",
          type: "value",
          splitNumber: 3,
          axisLabel: {
            textStyle: {
              color: "#ffffff"
            }
          }
        }
      ],
      series: [
        {
          name: "最高温度",
          itemStyle: {
            normal: {
              lineStyle: {
                color: "#FF0000"
              }
            }
          },
          data: maxTemperature,
          type: "line",
          symbol: "none",
          smooth: true
        },
        {
          name: "最低温度",
          data: minTemperature,
          type: "line",
          symbol: "none",
          smooth: true,
          itemStyle: {
            normal: {
              lineStyle: {
                color: "#00FF00"
              }
            }
          },
        },
        {
          name: "平均温度",
          data: avgTemperature,
          type: "line",
          symbol: "none",
          smooth: true,
          itemStyle: {
            normal: {
              lineStyle: {
                color: "#FF9900"
              }
            }
          },
        },
        {
          name: "降水量",
          data: precipitation,
          type: "bar",
          yAxisIndex: 1,
          itemStyle: {
            normal: {
              color: "#00CCFF"
            }
          },
        },
      ]
    }
    res.json(option)
  });
});

module.exports = router;
