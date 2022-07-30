from controller.forecast_api import forecast
from controller.forecast_api import get_all_forecast
from controller.year_api import get_all_year
from controller.history_api import get_history_weather

blueprint = [
    forecast.forecast_blueprint,
    get_all_forecast.get_all_forecast_blueprint,
    get_all_year.get_all_year_blueprint,
    get_history_weather.get_history_weather_blueprint
]
