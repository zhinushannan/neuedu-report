import json
import struct

import happybase

from flask import Blueprint

get_all_forecast_blueprint = Blueprint(
    'get_all_forecast',
    __name__,
    url_prefix='/api'
)


@get_all_forecast_blueprint.route("/get_all_forecast")
def get_all_forecast():
    maxTemperature = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
    minTemperature = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
    avgTemperature = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
    precipitation = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
    monthCount = [
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

    connection = happybase.Connection(host="master")
    forecast_table = connection.table("forecast")
    result = forecast_table.scan()
    for k, v in result:
        month = int(k.decode().split("/")[1]) - 1

        maxTemperature[month] += float(struct.unpack(">d", v[b"data:maxTemperature"])[0])
        minTemperature[month] += float(struct.unpack(">d", v[b"data:minTemperature"])[0])
        avgTemperature[month] += float(struct.unpack(">d", v[b"data:avgTemperature"])[0])
        precipitation[month] += float(struct.unpack(">d", v[b"data:precipitation"])[0])

        if struct.unpack(">d", v[b"data:maxTemperature"])[0] != 0.0:
            monthCount[month][0] += 1
        if struct.unpack(">d", v[b"data:minTemperature"])[0] != 0.0:
            monthCount[month][1] += 1
        if struct.unpack(">d", v[b"data:avgTemperature"])[0] != 0.0:
            monthCount[month][2] += 1
        if struct.unpack(">d", v[b"data:precipitation"])[0] != 0.0:
            monthCount[month][3] += 1

    for i in range(0, 12):
        maxTemperature[i] = round(maxTemperature[i] / monthCount[i][0], 2)
        minTemperature[i] = round(minTemperature[i] / monthCount[i][1], 2)
        avgTemperature[i] = round(avgTemperature[i] / monthCount[i][2], 2)
        precipitation[i] = round(precipitation[i] / monthCount[i][3], 2)

    maxTemperature_series = {
        "name": "最高温度",
        "itemStyle": {
            "normal": {
                "lineStyle": {
                    "color": "#FF0000"
                }
            }
        },
        "data": maxTemperature,
        "type": "line",
        "symbol": "none",
        "smooth": True
    }
    minTemperature_series = {
        "name": "最低温度",
        "data": minTemperature,
        "type": "line",
        "symbol": "none",
        "smooth": True,
        "itemStyle": {
            "normal": {
                "lineStyle": {
                    "color": "#00FF00"
                }
            }
        }
    }
    avgTemperature_series = {
        "name": "平均温度",
        "data": avgTemperature,
        "type": "line",
        "symbol": "none",
        "smooth": True,
        "itemStyle": {
            "normal": {
                "lineStyle": {
                    "color": "#FF9900"
                }
            }
        }
    }
    precipitation_series = {
        "name": "降水量",
        "data": precipitation,
        "type": "bar",
        "yAxisIndex": 1,
        "itemStyle": {
            "normal": {
                "color": "#00CCFF"
            }
        }
    }

    option = {
        "xAxis": {
            "data": [
                "1月",
                "2月",
                "3月",
                "4月",
                "5月",
                "6月",
                "7月",
                "8月",
                "9月",
                "10月",
                "11月",
                "12月"
            ],
            "axisLabel": {
                "textStyle": {
                    "color": "#ffffff"
                }
            }
        },
        "yAxis": [
            {
                "name": "温度(°C)",
                "splitNumber": 3,
                "axisLabel": {
                    "textStyle": {
                        "color": "#ffffff"
                    }
                }
            },
            {
                "name": "降水量(mm)",
                "type": "value",
                "splitNumber": 3,
                "axisLabel": {
                    "textStyle": {
                        "color": "#ffffff"
                    }
                }
            }
        ],
        "series": [
            maxTemperature_series,
            minTemperature_series,
            avgTemperature_series,
            precipitation_series
        ]

    }

    return json.dumps(option)
