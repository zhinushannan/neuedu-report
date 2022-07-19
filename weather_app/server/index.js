const express = require('express');
// 调用 express 函数，返回一个数据库实例

// 导入路由模块
const weatherApi = require('./api/weather_api');
const weatherYearApi = require("./api/weather_year_api")
const forecastApi = require("./api/forecast")

// 注册全局解析中间件
const app = express();

// 设置跨域
app.all('*', function (req, res, next) {
  // res.header("Access-Control-Allow-Origin", req.headers.origin);
  //设置允许跨域的域名，*代表允许任意域名跨域
  res.header("Access-Control-Allow-Origin", "*");
  //允许的header类型
  res.header("Access-Control-Allow-Headers", "content-type");
  //跨域允许的请求方式
  res.header("Access-Control-Allow-Methods", "DELETE,PUT,POST,GET,OPTIONS");
  if (req.method === "OPTIONS") res.send(200);/*让options请求快速返回*/
  else next();
});


app.use(express.json());
app.use(express.urlencoded({extended: false}));
// 注册路由模块
app.use('/api/', weatherApi, weatherYearApi, forecastApi);


// 调用 app.listen() 启动服务器
const port = 8888;
app.listen(port, () => console.log(`Example app listening on port 8888!`));
