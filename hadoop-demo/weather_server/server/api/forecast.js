let express = require('express');
const $forecast_sql = require("../sql/forecast");
const {sort} = require("../util/sort");
const conn = require("../config/mysql").conn
let router = express.Router();


// GET 请求
router.get('/get_all_forecast', (req, res) => {
    let query = $forecast_sql.query_all;
    conn.query(query, (err, result) => {
        for (let item in result) {
            result[item]["day"] = parseInt(result[item]["date"].split("/")[0])
            result[item]["month"] = parseInt(result[item]["date"].split("/")[1])
        }
        result.sort(sort("month"))

        let xData = ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
        let maxTemperature = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        let minTemperature = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        let avgTemperature = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        let precipitation = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        let monthCount = [
            [0, 0, 0, 0],
            [0, 0, 0, 0],
            [0, 0, 0, 0],
            [0, 0, 0, 0],
            [0, 0, 0, 0],
            [0, 0, 0, 0],
            [0, 0, 0, 0],
            [0, 0, 0, 0],
            [0, 0, 0, 0],
            [0, 0, 0, 0],
            [0, 0, 0, 0],
            [0, 0, 0, 0],
        ]


        for (let item in result) {
            let month = result[item]["month"] - 1
            monthCount[month][0] = result[item]["maxTemperature"] === 0 ? monthCount[month][0] : monthCount[month][0] + 1
            monthCount[month][1] = result[item]["minTemperature"] === 0 ? monthCount[month][1] : monthCount[month][1] + 1
            monthCount[month][2] = result[item]["avgTemperature"] === 0 ? monthCount[month][2] : monthCount[month][2] + 1
            monthCount[month][3] = result[item]["precipitation"] === 0 ? monthCount[month][3] : monthCount[month][3] + 1
            maxTemperature[month] += result[item]["maxTemperature"]
            minTemperature[month] += result[item]["minTemperature"]
            avgTemperature[month] += result[item]["avgTemperature"]
            precipitation[month] += result[item]["precipitation"]
        }

        for (let i = 0; i < 12; i++) {
            maxTemperature[i] = Math.round((maxTemperature[i] / monthCount[i][0]) * 10) / 10
            minTemperature[i] = Math.round((minTemperature[i] / monthCount[i][1]) * 10) / 10
            avgTemperature[i] = Math.round((avgTemperature[i] / monthCount[i][2]) * 10) / 10
            precipitation[i] = Math.round((precipitation[i] / monthCount[i][3]) * 10) / 10
        }

        let option = {
            xAxis: {
                data: xData,
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


router.get('/forecast', (req, res) => {
    let query = $forecast_sql.query_by_date
    let toDay = new Date()

    let target = []
    for (let i = 0; i < 7; i++) {
        toDay = new Date(toDay.getTime() + 24 * 60 * 60 * 1000)
        let date = toDay.getDate().toString().padStart(2, "0") + "/" + (toDay.getMonth() + 1).toString().padStart(2, "0")
        target.push(date)
    }

    conn.query(query, [[target]], (err, result) => {
        for (let item in result) {
            result[item]["month"] = parseInt(result[item]["date"].split("/")[1])
            result[item]["dateOfMonth"] = parseInt(result[item]["date"].split("/")[0])
        }
        result.sort(sort("month"))

        let date = []
        let maxTemperature = []
        let minTemperature = []
        let avgTemperature = []
        let precipitation = []

        for (let item in result) {
            let data = result[item]
            date.push(data["date"].split("/")[1] + "月" + data["date"].split("/")[0] + "日")
            maxTemperature.push(data["maxTemperature"])
            minTemperature.push(data["minTemperature"])
            avgTemperature.push(data["avgTemperature"])
            precipitation.push(data["precipitation"])
        }

        let option = {
            xAxis: {
                data: date,
                axisLabel: {
                    textStyle: {
                        color: "#ffffff"
                    }
                }
            },
            yAxis: [
                {
                    name: "温度(°C)",
                    min: 0,
                    max: 40,
                    splitNumber: 4,
                    axisLabel: {
                        textStyle: {
                            color: "#ffffff"
                        }
                    }
                },
                {
                    name: "降水量(mm)",
                    type: "value",
                    splitNumber: 4,
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
    })

})

module.exports = router;
