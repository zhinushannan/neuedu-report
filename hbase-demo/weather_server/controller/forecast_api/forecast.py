import json
import struct
from datetime import datetime, timedelta

import happybase

from flask import Blueprint

forecast_blueprint = Blueprint(
    'forecast_api',
    __name__,
    url_prefix='/api'
)


@forecast_blueprint.route('/forecast')
def forecast():
    target_days = []

    date = datetime.today()
    for i in range(1, 8):
        date = date + timedelta(days=1)
        month = str(date.month).zfill(2)
        day = str(date.day).zfill(2)
        target_days.append(month + "月" + day + "日")

    maxTemperature_series = {
        "name": "最高温度",
        "itemStyle": {
            "normal": {
                "lineStyle": {
                    "color": "#FF0000"
                }
            }
        },
        "data": [],
        "type": "line",
        "symbol": "none",
        "smooth": True
    }
    minTemperature_series = {
        "name": "最低温度",
        "data": [],
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
        "data": [],
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
        "data": [],
        "type": "bar",
        "yAxisIndex": 1,
        "itemStyle": {
            "normal": {
                "color": "#00CCFF"
            }
        }
    }
    connection = happybase.Connection(host="master")
    forecast_table = connection.table("forecast")
    for i in target_days:
        row_key = i.replace("日", "")
        row_key = "83377_" + row_key.split("月")[1] + "/" + row_key.split("月")[0]
        row = forecast_table.row(row_key, columns=["data:maxTemperature", "data:minTemperature", "data:avgTemperature",
                                                   "data:precipitation"])
        maxTemperature_series["data"].append(struct.unpack(">d", row[b"data:maxTemperature"])[0])
        minTemperature_series["data"].append(struct.unpack(">d", row[b"data:minTemperature"])[0])
        avgTemperature_series["data"].append(struct.unpack(">d", row[b"data:avgTemperature"])[0])
        precipitation_series["data"].append(struct.unpack(">d", row[b"data:precipitation"])[0])
    connection.close()

    option = {
        "xAxis": {
            "data": target_days,
            "axisLabel": {
                "textStyle": {
                    "color": "#ffffff"
                }
            }
        },
        "yAxis": [
            {
                "name": "温度(°C)",
                "min": 0,
                "max": 40,
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
                "axisLabel": {
                    "textStyle": {
                        "color": "#ffffff"
                    }
                }
            }
        ],
        "series": [maxTemperature_series, minTemperature_series, avgTemperature_series, precipitation_series]
    }

    return json.dumps(option)
