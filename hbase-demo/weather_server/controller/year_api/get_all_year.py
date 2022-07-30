import json
import struct

import happybase

from flask import Blueprint

get_all_year_blueprint = Blueprint(
    'get_all_year',
    __name__,
    url_prefix='/api'
)


@get_all_year_blueprint.route("/get_all_year")
def get_all_year():
    year = []
    maxTemperature = []
    minTemperature = []
    avgTemperature = []
    precipitationTotal = []
    rainDays = []

    connection = happybase.Connection(host="master")
    forecast_table = connection.table("weathersummary")
    result = forecast_table.scan()
    for k, v in result:
        year.append(v[b"data:year"].decode())
        maxTemperature.append(round(struct.unpack(">d", v[b"data:maxTemperature"])[0], 2))
        minTemperature.append(round(struct.unpack(">d", v[b"data:minTemperature"])[0], 2))
        avgTemperature.append(round(struct.unpack(">d", v[b"data:avgTemperature"])[0], 2))
        precipitationTotal.append(round(struct.unpack(">d", v[b"data:precipitationTotal"])[0], 2))
        rainDays.append(struct.unpack(">i", v[b"data:rainDays"])[0])
    connection.close()

    option = {
        "xAxis": {
            "data": year,
            "axisLabel": {
                "textStyle": {
                    "color": "#ffffff"
                }
            }
        },
        "yAxis": [
            {
                "name": "温度(°C)",
                "splitNumber": 4,
                "min": 0,
                "max": 40,
                "axisLabel": {
                    "textStyle": {
                        "color": "#ffffff"
                    }
                }
            },
            {
                "name": "年降水量(mm)",
                "splitNumber": 4,
                "min": 0,
                "max": 2000,
                "axisLabel": {
                    "textStyle": {
                        "color": "#ffffff"
                    }
                }
            },
            {
                "name": "年降雨天数",
                "max": 200,
                "min": 0,
                "splitNumber": 4,
                "show": False
            }
        ],
        "series": [
            {
                "name": "年最高温度",
                "type": "line",
                "smooth": True,
                "symbol": "none",
                "itemStyle": {
                    "normal": {
                        "lineStyle": {
                            "color": "#FF0000"
                        }
                    }
                },
                "data": maxTemperature
            },
            {
                "name": "年最低温度",
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
            },
            {
                "name": "年平均温度",
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
            },
            {
                "name": "年降水量",
                "data": precipitationTotal,
                "type": "bar",
                "yAxisIndex": 1,
                "itemStyle": {
                    "normal": {
                        "color": "#00CCFF"
                    }
                }
            },
            {
                "name": "年降雨天数",
                "data": rainDays,
                "type": "bar",
                "yAxisIndex": 2,
                "itemStyle": {
                    "normal": {
                        "color": "#FFFFCC"
                    }
                }
            }
        ]
    }

    return json.dumps(option)
