import happybase
from flask import Flask

app = Flask(__name__)


@app.route('/api/forecast')
def hello_world():
    connection = happybase.Connection(host="master")
    forecast_table = connection.table("forecast")


    connection.close()
    return 'Hello World!'


if __name__ == '__main__':
    app.run()
