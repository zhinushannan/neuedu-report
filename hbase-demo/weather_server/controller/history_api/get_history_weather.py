import json
import struct

import happybase
from datetime import datetime

from flask import Blueprint

get_history_weather_blueprint = Blueprint(
    'get_history_weather',
    __name__,
    url_prefix='/api'
)


@get_history_weather_blueprint.route("/get_history_weather")
def get_history_weather():
    year = []
    maxTemperature = []
    minTemperature = []
    avgTemperature = []
    precipitation = []

    connection = happybase.Connection(host="master")
    forecast_table = connection.table("weather")
    result = forecast_table.scan(
        filter=f"RowFilter(=, 'regexstring:^83377_{str(datetime.today().day).zfill(2)}/{str(datetime.today().month).zfill(2)}/')")
    for k, v in result:
        year.append(k.decode().split("/")[2])
        maxTemperature.append(struct.unpack(">d", v[b"data:maxTemperature"])[0])
        minTemperature.append(struct.unpack(">d", v[b"data:minTemperature"])[0])
        avgTemperature.append(struct.unpack(">d", v[b"data:avgTemperature"])[0])
        precipitation.append(struct.unpack(">d", v[b"data:precipitation"])[0])
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
                "axisLabel": {
                    "textStyle": {
                        "color": "#ffffff"
                    }
                }
            },
            {
                "name": "降水量(mm)",
                "type": "value",
                "splitNumber": 4,
                "min": 0,
                "max": 20,
                "axisLabel": {
                    "textStyle": {
                        "color": "#ffffff"
                    }
                }
            }
        ],
        "series": [
            {
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
            },
            {
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
            },
            {
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
            },
            {
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
        ]
    }

    return json.dumps(option)
