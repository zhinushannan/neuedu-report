let express = require('express');
const $weather_year_api = require("../sql/weather_year");
const {sort} = require("../util/sort");
const conn = require("../config/mysql").conn
let router = express.Router();

router.get('/get_all_year', (req, res) => {
  let query = $weather_year_api.query_all;
  conn.query(query, (err, result) => {

    result.sort(sort("year"))

    let year = []
    let maxTemperature = []
    let minTemperature = []
    let avgTemperature = []
    let precipitation = []
    let rainDays = []

    for (let item in result) {
      year.push(result[item]["year"])
      maxTemperature.push(result[item]["maxTemperature"])
      minTemperature.push(result[item]["minTemperature"])
      avgTemperature.push(result[item]["avgTemperature"])
      precipitation.push(result[item]["precipitation"])
      rainDays.push(result[item]["rainDays"])
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
          splitNumber: 4,
          axisLabel: {
            textStyle: {
              color: "#ffffff"
            }
          }
        },
        {
          name: "年降水量(mm)",
          splitNumber: 4,
          axisLabel: {
            textStyle: {
              color: "#ffffff"
            }
          }
        },
        {
          name: "年降雨天数",
          splitNumber: 5,
          show: false
        }
      ],
      series: [
        {
          name: "年最高温度",
          type: "line",
          smooth: true,
          symbol: "none",
          itemStyle: {
            normal: {
              lineStyle: {
                color: "#FF0000"
              }
            }
          },
          data: maxTemperature,

        },
        {
          name: "年最低温度",
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
          name: "年平均温度",
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
          name: "年降水量",
          data: precipitation,
          type: "bar",
          yAxisIndex: 1,
          itemStyle: {
            normal: {
                color: "#00CCFF"
            }
          },
        },
        {
          name: "年降雨天数",
          data: rainDays,
          type: "bar",
          yAxisIndex: 2,
          itemStyle: {
            normal: {
              color: "#FFFFCC"
            }
          }
        }
      ]
    }


    res.json(option)
  });
})

module.exports = router;
