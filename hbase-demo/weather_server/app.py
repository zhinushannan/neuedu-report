from flask import Flask
from flask_cors import CORS

import controller

app = Flask(__name__)
CORS(app, resources=r'/*')

for i in controller.blueprint:
    app.register_blueprint(i)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8888, debug=False)
