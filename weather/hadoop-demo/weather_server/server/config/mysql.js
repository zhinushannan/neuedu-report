// 连接数据库配置
const mysql = require("mysql");

let mysql_info = {
  host: 'localhost', // 域名，这是本机域名
  user: 'root', // MySQL 登录用户名
  password: '09140727', // MySQL 登录密码 一般都是 root
  database: 'weather', // 要连接的数据库名
  port: '3307', // 数据库端口号
  insecureAuth: true
}

// 连接数据库
let conn = mysql.createConnection(mysql_info);
conn.connect();

exports.conn = conn
