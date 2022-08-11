# 基于Hadoop的巴西利亚天气可视化与预测项目

## 一、项目简介

## 二、需求分析及界面设计

### 1、需求分析

根据所给数据集（巴西所有地区1961-2019年气象数据），利用HDFS、MapReduce、NodeJs、Vue.js、E-Charts等技术，完成对数据的分析、可视化与天气预测功能。

数据集说明如下：


| 序号 |                 葡萄牙文字段 - 英文字段                  |         中文描述         |
| :--: | :------------------------------------------------------: | :----------------------: |
|  **1**   |              **Esracao - Weather station sode**              |      **气象监测站代码**      |
|  **2**   |                 **Data - Date (dd/MM/YYYY)**                 |           **日期**           |
|  **3**   |                    **Hora - Hour (%H%M)**                    |           **小时**           |
|  **4**   |            **Precipitacao - Precipitation (mm)**             |          **降水量**          |
|  5   |        TempBulboSeco - Dry bulb temperature (°C)         |         干球湿度         |
|  6   |        TempBulboUmido - Wet bulb temperature (°C)        |         湿球湿度         |
|  **7**   |         **UmidadeRelativa - Relative humidity (%)**          |         **最高温度**         |
|  **8**   |          **TempMinima - Minimum temperature (°C)**           |         **最低温度**         |
|  9   |         UmidadeRelativa - Relative humidity (%)          |         相对湿度         |
|  10  | PressaoAtmEstacao - Station Atmospheric Pressure (mbar)  |        站大气压力        |
|  11  | PressaoAtmMar - Atmospheric pressure at sea level (mbar) |      海平面的大气压      |
|  12  |          DirecaoVento - Wind Direction (tabela)          |           风向           |
|  13  |            VelocidadeVento - Wind speed (m/s)            |           风速           |
|  14  |               Insolacao - Insolation (hs)                |           日照           |
|  15  |            Nebulosidade - Cloudiness (tenths)            |           云量           |
|  16  |        Evaporacao Piche - Piche Evaporation (mm)         |          蒸发量          |
|  **17**  |  **Temp Comp Media - Average Compensated Temperature (°C)**  | **平均补偿温度（平均温度）** |
|  18  |  Umidade Relativa Media - Avarage Relative Humidity (%)  | 平均相对湿度（平均湿度） |
|  19  |   Velocidade do Vento Media - Average Wind Speed (mps)   |         平均风速         |

### 2、界面设计

#### 整体概述

使用Vue.js完成前端的快速搭建，利用E-Charts实现数据的可视化，所示结果为弹性布局，可根据屏幕大小变化实时调整。

界面共分为四个模块，分别为一周天气预报，历史上的今天，天气走向曲线，年最高、最低、平均气温。每个模块都综合运用了平滑曲线图和柱状图，且支持鼠标移动到对应数据上显示数据。

一周天气预报：展示对从次日起七日内的最高温度、最低温度、平均温度和降水量的预测的结果。

历史上的今天：展示历史上的今天的最高温度、最低温度、平均温度和降水量的记录值。

天气走向曲线：根据历史数据，计算出每个月的平均最高气温、平均最低气温、平均平均气温、月总降水量，并进行可视化展示。

年最高、最低、平均气温：根据历史数据，计算出每年的最高气温、最低气温、平均平均气温、年总降水量、年总降水天数，并进行可视化展示，此模块支持可视化图的交互拉拽。

![image-20220719161007112](https://picgo.kwcoder.club/202206/202207191610723.png)

## 三、功能设计

### 1、模拟天气预测

模拟预测天气预报，生成一周的天气预测结果并进行可视化展示。

![image-20220719162244043](https://picgo.kwcoder.club/202206/202207191622401.png)

### 2、历史天气查询

展示历史上今天的天气数据。

![image-20220719162321230](https://picgo.kwcoder.club/202206/202207191623496.png)

### 3、天气走向曲线

根据历史数据，计算出每年1-12月份每月气温和降水的趋势。

![image-20220719162357912](https://picgo.kwcoder.club/202206/202207191623131.png)

### 4、历史年份数据查询

通过“拖拉拽”的方式在`年最高、最低、平均温度`模块中，可以查看每年的最高气温、最低气温、平均气温、年总降水量和年总降水天数。

![image-20220719161516639](https://picgo.kwcoder.club/202206/202207191615207.png)

### 5、页面自适应

页面可以根据窗口大小的动态变化主动实时调整图例大小。

![image-20220719162016209](https://picgo.kwcoder.club/202206/202207191620968.png)

![image-20220719162041710](https://picgo.kwcoder.club/202206/202207191620179.png)

## 四、详细设计

大数据处理流程主要包括数据收集、数据预处理、数据存储、数据处理与分析、数据展示/数据可视化、数据应用等环节，其中数据质量贯穿于整个大数据流程，每一个数据处理环节都会对大数据质量产生影响作用。

通常，一个好的大数据产品要有大量的数据规模、快速的数据处理、精确的数据分析与预测、优秀的可视化图表以及简练易懂的结果解释。

### 1、数据导入

#### 准备数据集：

数据集来源：[https://www.kaggle.com/saraivaufc/conventional-weather-stations-brazil#conventional_weather_stations_inmet_brazil_1961_2019.csv](https://www.kaggle.com/saraivaufc/conventional-weather-stations-brazil#conventional_weather_stations_inmet_brazil_1961_2019.csv)

或通过`Release`下载：[https://github.com/zhinushannan/weather/releases/tag/data](https://github.com/zhinushannan/weather/releases/tag/data)

#### 数据导入：

通过HDFS的WEB端上传数据

![image-20220719164105691](https://picgo.kwcoder.club/202206/202207191641858.png)

### 2、大数据统计与分析

在这部分中，主要使用了MapReduce技术，Hadoop集群搭建过程参照[基于CentOS7镜像和数据挂载卷实现Docker搭建Hadoop集群](https://dream.kwcoder.club/p/20220626/)。

#### 处理思路

1. 清洗数据：只保留`83377`观测站的数据，并将非法数据剔除，将每一天的数据合并，缩小数据量。经过估计，处理后的数据大约在两万行左右，可以使用MySQL数据库存储，以方便后续的利用。
2. 数据统计：将天气数据按年统计，计算出每年的最高气温、最低气温、平均气温、总降水量、总降水天数，并输出到数据库。
3. 数据分析：根据历史天气数据，寻找每一天的天气的综合规律，依据此进行天气预测。
4. 数据可视化：利用Nodejs技术快速搭建后台、Vue.js搭建前端，快速开发大屏可视化端。

#### 代码设计

大数据端需要多步计算，需要写多个MapReduce，因此可以利用反射机制去编写一个统一启动类。同时，因为需要频繁的进行数据校验、相关实例的获取，可以编写工具类来简化开发。

在应用端，由于业务量较少，因此可以使用Nodejs进行快速搭建后台，使用`express`框架。前端使用Vue.js框架，便于向后端发送异步请求。使用Nodejs和Vue.js开发，可以使用一个npm进行项目管理。

### 3、工具类代码实现

#### `HadoopUtils.java`

用于获取hadoop配置对象

```java
package club.kwcoder.weather.util;

import org.apache.hadoop.conf.Configuration;

/**
 * Hadoop 工具类，用于获取hadoop配置对象
 *
 * @author zhinushannan
 */
public class HadoopUtils {

    /**
     * hadoop配置
     */
    private static final Configuration conf;

    static {
        conf = new Configuration();
    }

    public static Configuration getConf() {
        return conf;
    }

}
```

#### `ValidateUtils.java`

校验工具类

```java
package club.kwcoder.weather.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 校验工具类，统一返回方式，针对本项目，合法返回false，非法返回true
 *
 * @author zhinushannan
 */
public class ValidateUtils {

    /**
     * 校验输入文本数据
     *
     * @param line 文本数据
     * @return 空返回true
     */
    public static boolean validate(String line) {
        return StringUtils.isBlank(line);
    }

    /**
     * 校验字符串数组长度
     *
     * @param items  字符串数组
     * @param length 长度
     * @return 不相同返回true
     */
    public static boolean validate(String[] items, int length) {
        return items.length != length;
    }

}
```

### 4、实体类代码编写

#### `ForecastWritable.java`

```java
package club.kwcoder.weather.writable;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.lib.db.DBWritable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class ForecastWritable implements DBWritable, Writable {

    /**
     * 日期字符串，格式—— dd/MM/yyyy
     */
    private String date;

    /**
     * 最高温度
     */
    private Float maxTemperature;

    /**
     * 最低温度
     */
    private Float minTemperature;

    /**
     * 平均温度
     */
    private Float avgTemperature;

    /**
     * 降水量
     */
    private Float precipitation;

    // 省略空参构造、满参构造、getter/setter、equals/hashcode
  
    /**
     * 通过建造者对象获取实例
     *
     * @param builder 对象实例
     */
    public ForecastWritable(Builder builder) {
        this.date = builder.date;
        this.maxTemperature = builder.maxTemperature;
        this.minTemperature = builder.minTemperature;
        this.avgTemperature = builder.avgTemperature;
        this.precipitation = builder.precipitation;
    }

    /**
     * 建造者类
     */
    public static class Builder {
        /**
         * 日期字符串，格式—— dd/MM/yyyy
         */
        private String date;

        /**
         * 最高温度
         */
        private Float maxTemperature;

        /**
         * 最低温度
         */
        private Float minTemperature;

        /**
         * 平均温度
         */
        private Float avgTemperature;

        /**
         * 降水量
         */
        private Float precipitation;

        /**
         * 设置日期字符串
         */
        public Builder setDate(String date) {
            this.date = date;
            return this;
        }

        /**
         * 设置最高温度
         */
        public Builder setMaxTemperature(Float maxTemperature) {
            this.maxTemperature = (float) (Math.round(maxTemperature * 10) / 10);
            return this;
        }

        /**
         * 设置最低温度
         */
        public Builder setMinTemperature(Float minTemperature) {
            this.minTemperature = (float) (Math.round(minTemperature * 10) / 10);
            return this;
        }

        /**
         * 设置平均温度
         */
        public Builder setAvgTemperature(Float avgTemperature) {
            this.avgTemperature = (float) (Math.round(avgTemperature * 10) / 10);
            return this;
        }

        /**
         * 设置降水量
         */
        public Builder setPrecipitation(Float precipitation) {
            this.precipitation = (float) (Math.round(precipitation * 10) / 10);
            return this;
        }

        /**
         * 获取建造者对象
         */
        public Builder() {
        }

        /**
         * 建造，获取对象实例
         */
        public ForecastWritable build() {
            return new ForecastWritable(this);
        }

    }

    /**
     * 数据库表明
     */
    public static final String tableName = "forecast";

    /**
     * 数据库字段
     */
    public static final String[] fields = {"date", "maxTemperature", "minTemperature", "avgTemperature", "precipitation"};

    /**
     * 数据库输出序列化
     */
    @Override
    public void write(PreparedStatement statement) throws SQLException {
        statement.setString(1, this.date);
        statement.setFloat(2, this.maxTemperature);
        statement.setFloat(3, this.minTemperature);
        statement.setFloat(4, this.avgTemperature);
        statement.setFloat(5, this.precipitation);
    }

    /**
     * 数据库输入反序列化
     */
    @Override
    public void readFields(ResultSet resultSet) throws SQLException {
        this.date = resultSet.getString(1);
        this.maxTemperature = resultSet.getFloat(2);
        this.minTemperature = resultSet.getFloat(3);
        this.avgTemperature = resultSet.getFloat(4);
        this.precipitation = resultSet.getFloat(5);
    }

    /**
     * 文件输出序列化
     */
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(this.date);
        out.writeFloat(this.maxTemperature);
        out.writeFloat(this.minTemperature);
        out.writeFloat(this.avgTemperature);
        out.writeFloat(this.precipitation);
    }

    /**
     * 文件输入反序列化
     */
    @Override
    public void readFields(DataInput in) throws IOException {
        this.date = in.readUTF();
        this.maxTemperature = in.readFloat();
        this.minTemperature = in.readFloat();
        this.avgTemperature = in.readFloat();
        this.precipitation = in.readFloat();
    }

    @Override
    public String toString() {
        return date + '\t' + maxTemperature + '\t' + minTemperature + '\t' + avgTemperature + '\t' + precipitation;
    }

}
```

#### `WeatherWritable.java`

```java
package club.kwcoder.weather.writable;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.lib.db.DBWritable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class WeatherWritable implements WritableComparable<WeatherWritable>, DBWritable {

    /**
     * 气象站代码
     */
    private String code;
    /**
     * 日期
     */
    private String date;
    /**
     * 降水量
     */
    private Float precipitation;

    /**
     * 最高温度
     */
    private Float maxTemperature;
    /**
     * 最低温度
     */
    private Float minTemperature;
    /**
     * 平均温度
     */
    private Float avgTemperature;
  
    // 省略空参构造、满参构造、getter/setter、equals/hashcode

    /**
     * 通过建造者对象获取实例
     *
     * @param builder 对象实例
     */
    public WeatherWritable(Builder builder) {
        this.code = builder.code;
        this.date = builder.date;
        this.precipitation = builder.precipitation;
        this.maxTemperature = builder.maxTemperature;
        this.minTemperature = builder.minTemperature;
        this.avgTemperature = builder.avgTemperature;
    }

    public static class Builder {
        /**
         * 气象站代码
         */
        private String code;
        /**
         * 日期
         */
        private String date;
        /**
         * 降水量
         */
        private Float precipitation;

        /**
         * 最高温度
         */
        private Float maxTemperature;
        /**
         * 最低温度
         */
        private Float minTemperature;
        /**
         * 平均温度
         */
        private Float avgTemperature;

        /**
         * 设置气象站代码
         */
        public Builder setCode(String code) {
            this.code = code;
            return this;
        }

        /**
         * 设置日期
         */
        public Builder setDate(String date) {
            this.date = date;
            return this;
        }

        /**
         * 设置降水量
         */
        public Builder setPrecipitation(Float precipitation) {
            this.precipitation = precipitation;
            return this;
        }

        /**
         * 设置最高温度
         */
        public Builder setMaxTemperature(Float maxTemperature) {
            this.maxTemperature = maxTemperature;
            return this;
        }

        /**
         * 设置最低温度
         */
        public Builder setMinTemperature(Float minTemperature) {
            this.minTemperature = minTemperature;
            return this;
        }

        /**
         * 这是平均温度
         */
        public Builder setAvgTemperature(Float avgTemperature) {
            this.avgTemperature = avgTemperature;
            return this;
        }

        /**
         * 获取建造者对象
         */
        public Builder() {
        }

        /**
         * 建造，获取对象实例
         */
        public WeatherWritable build() {
            return new WeatherWritable(this);
        }

    }


    /**
     * 数据库表明
     */
    public static final String tableName = "weather";

    /**
     * 数据库字段
     */
    public static final String[] fields = {"code", "date", "precipitation", "maxTemperature",
            "minTemperature", "avgTemperature"};

    /**
     * 数据库输出序列化
     */
    @Override
    public void write(PreparedStatement statement) throws SQLException {
        statement.setString(1, this.code);
        statement.setString(2, this.date);
        statement.setFloat(3, this.precipitation);
        statement.setFloat(4, this.maxTemperature);
        statement.setFloat(5, this.minTemperature);
        statement.setFloat(6, this.avgTemperature);
    }

    /**
     * 数据库输入反序列化
     */
    @Override
    public void readFields(ResultSet resultSet) throws SQLException {
        this.code = resultSet.getString(1);
        this.date = resultSet.getString(2);
        this.precipitation = resultSet.getFloat(3);
        this.maxTemperature = resultSet.getFloat(4);
        this.minTemperature = resultSet.getFloat(5);
        this.avgTemperature = resultSet.getFloat(6);
    }

    /**
     * 文件输出序列化
     */
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(this.code);
        out.writeUTF(this.date);
        out.writeFloat(this.precipitation);
        out.writeFloat(this.maxTemperature);
        out.writeFloat(this.minTemperature);
        out.writeFloat(this.avgTemperature);
    }

    /**
     * 文件输入反序列化
     */
    @Override
    public void readFields(DataInput in) throws IOException {
        this.code = in.readUTF();
        this.date = in.readUTF();
        this.precipitation = in.readFloat();
        this.maxTemperature = in.readFloat();
        this.minTemperature = in.readFloat();
        this.avgTemperature = in.readFloat();
    }

    @Override
    public int compareTo(WeatherWritable other) {
        if (null == other) {
            return 1;
        }
        // 仅考虑83377巴西利亚的数据
        return this.date.compareTo(other.getDate());
    }

    @Override
    public String toString() {
        return code + '\t' + date + '\t' + precipitation + '\t' + maxTemperature + '\t' + minTemperature + '\t' + avgTemperature;
    }

}
```

#### `WeatherWritableSummary.java`

```java
package club.kwcoder.weather.writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class WeatherWritableSummary extends WeatherWritable {
    /**
     * 年份
     */
    private String year;
    /**
     * 降雨天数
     */
    private Integer rainDays;

    // 省略空参构造、满参构造、getter/setter、equals/hashcode
  
    /**
     * 通过建造者对象获取实例
     *
     * @param builder 对象实例
     */
    public WeatherWritableSummary(Builder builder) {
        super(builder);
        this.year = builder.year;
        this.rainDays = builder.rainDays;
    }

    /**
     * 数据库表明
     */
    public static final String tableName = "weather_year";

    /**
     * 数据库字段
     */
    public static final String[] fields = {"code", "precipitation", "maxTemperature",
            "minTemperature", "avgTemperature", "year", "rainDays"};

    /**
     * 数据库输出序列化
     */
    @Override
    public void write(PreparedStatement statement) throws SQLException {
        System.out.println(this);
        statement.setString(1, super.getCode());
        statement.setFloat(2, super.getPrecipitation());
        statement.setFloat(3, super.getMaxTemperature());
        statement.setFloat(4, super.getMinTemperature());
        statement.setFloat(5, super.getAvgTemperature());
        statement.setString(6, this.year);
        statement.setInt(7, this.rainDays);
    }

    /**
     * 数据库输入反序列化
     */
    @Override
    public void readFields(ResultSet resultSet) throws SQLException {
        super.setCode(resultSet.getString(1));
        super.setPrecipitation(resultSet.getFloat(2));
        super.setMaxTemperature(resultSet.getFloat(3));
        super.setMinTemperature(resultSet.getFloat(4));
        super.setAvgTemperature(resultSet.getFloat(5));
        this.year = resultSet.getString(6);
        this.rainDays = resultSet.getInt(7);
    }

    /**
     * 文件输出序列化
     */
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(this.year);
        out.writeInt(this.rainDays);
        out.writeUTF(super.getCode());
        out.writeFloat(super.getPrecipitation());
        out.writeFloat(super.getMaxTemperature());
        out.writeFloat(super.getMinTemperature());
        out.writeFloat(super.getAvgTemperature());
    }

    /**
     * 文件输入反序列化
     */
    @Override
    public void readFields(DataInput in) throws IOException {
        this.year = in.readUTF();
        this.rainDays = in.readInt();
        super.setCode(in.readUTF());
        super.setPrecipitation(in.readFloat());
        super.setMaxTemperature(in.readFloat());
        super.setMinTemperature(in.readFloat());
        super.setAvgTemperature(in.readFloat());
    }

    @Override
    public String toString() {
        return super.getCode() + '\t' + this.getYear() + '\t' + super.getPrecipitation() + '\t' + super.getMaxTemperature() + '\t' + super.getMinTemperature() + "\t" + super.getAvgTemperature() + "\t" + this.rainDays;
    }

    public static class Builder extends WeatherWritable.Builder {
        /**
         * 年份
         */
        private String year;
        /**
         * 降雨天数
         */
        private Integer rainDays;

        /**
         * 设置降雨天数
         */
        public Builder setRainDays(Integer rainDays) {
            this.rainDays = rainDays;
            return this;
        }

        /**
         * 设置年份
         */
        public Builder setYear(String year) {
            this.year = year;
            return this;
        }

        /**
         * 设置气象站代码
         */
        public Builder setCode(String code) {
            super.setCode(code);
            return this;
        }

        /**
         * 设置降水量
         */
        public Builder setPrecipitation(Float precipitation) {
            super.setPrecipitation((float) (Math.round(precipitation * 10) / 10));
            return this;
        }

        /**
         * 设置最高温度
         */
        public Builder setMaxTemperature(Float maxTemperature) {
            super.setMaxTemperature(maxTemperature);
            return this;
        }

        /**
         * 设置最低温度
         */
        public Builder setMinTemperature(Float minTemperature) {
            super.setMinTemperature(minTemperature);
            return this;
        }

        /**
         * 这是平均温度
         */
        public Builder setAvgTemperature(Float avgTemperature) {
            super.setAvgTemperature((float) (Math.round(avgTemperature * 10) / 10));
            return this;
        }

        /**
         * 建造，获取对象实例
         */
        public WeatherWritableSummary buildSummary() {
            return new WeatherWritableSummary(this);
        }

        /**
         * 获取建造者对象
         */
        public Builder() {
        }

    }

}
```

### 5、运行类代码编写

#### `DataCleaning.java`

```java
package club.kwcoder.weather.runner;

import club.kwcoder.weather.WeatherStarter;
import club.kwcoder.weather.util.HadoopUtils;
import club.kwcoder.weather.util.ValidateUtils;
import club.kwcoder.weather.writable.WeatherWritable;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.lib.db.DBOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 第一步：数据清洗，数据验证，数据导入，三行合一
 * 输出格式：监测站代码、日期、降水量、最高温度、最低温度、平均温度
 */
public class DataCleaning {

    public static void runner() {
        Configuration conf = HadoopUtils.getConf();
        DBConfiguration.configureDB(
                conf,
                WeatherStarter.MysqlDriverClass,
                WeatherStarter.MysqlUrl,
                WeatherStarter.username,
                WeatherStarter.password
        );

        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName(WeatherStarter.MysqlDriverClass);
            connection = DriverManager.getConnection(WeatherStarter.MysqlUrl, WeatherStarter.username, WeatherStarter.password);
            statement = connection.createStatement();
            String sql = "delete from weather where 1=1";
            statement.executeQuery(sql);
        } catch (Exception ignore) {
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ignore) {}
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ignore) {}
            }
        }

        try {
            Job job = Job.getInstance(conf, "data_cleaning");
            // 设置执行类
            job.setJarByClass(DataCleaning.class);
            // 设置输入
            job.setInputFormatClass(TextInputFormat.class);
            FileInputFormat.setInputPaths(job, "/weather");
            // 设置Mapper
            job.setMapperClass(DataCleanMapper.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(WeatherWritable.class);
            // 设置Reducer
            job.setReducerClass(DataCleanReducer.class);
            job.setOutputKeyClass(WeatherWritable.class);
            job.setOutputValueClass(NullWritable.class);
            // 设置输出
            job.setOutputFormatClass(DBOutputFormat.class);
            DBOutputFormat.setOutput(
                    job,
                    WeatherWritable.tableName,
                    WeatherWritable.fields
            );
            // 运行
            boolean flag = job.waitForCompletion(true);
            if (flag) {
                System.out.println("data_cleaning process success");
            }
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static class DataCleanMapper extends Mapper<LongWritable, Text, Text, WeatherWritable> {

        private static final Text outKey = new Text();
        WeatherWritable.Builder builder = new WeatherWritable.Builder();

        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, WeatherWritable>.Context context) throws IOException, InterruptedException {
            String line = value.toString();
            if (ValidateUtils.validate(line)) {
                return;
            }
            // 跳过标题行：key为0指的是每一块的第一个字符，不是整个文件的，不能只使用key判断
            if (key.equals(new LongWritable(0L)) && line.startsWith("Estacao")) {
                return;
            }
            // 不选择83377巴西利亚的数据
            if (!line.contains("83377")) {
                return;
            }
            /*
            拆分数据：如果指使用;，则在没有数据的地方会直接跳过，即";;;;;".split(";").length为0
            使用强制拆分，在满足条件的情况下拆分成19列
             */
            String[] items = line.split(";", 19);
            // 块切分时可能会把一行切在两块中
            if (ValidateUtils.validate(items, 19)) {
                return;
            }
            WeatherWritable weatherWritable = builder
                    .setCode(items[0])
                    .setDate(items[1])
                    .setPrecipitation(StringUtils.isBlank(items[3]) ? 0 : Float.parseFloat(items[3]))
                    .setMaxTemperature(StringUtils.isBlank(items[6]) ? 0 : Float.parseFloat(items[6]))
                    .setMinTemperature(StringUtils.isBlank(items[7]) ? 0 : Float.parseFloat(items[7]))
                    .setAvgTemperature(StringUtils.isBlank(items[16]) ? 0 : Float.parseFloat(items[16]))
                    .build();
            // <83377-01/01/1961, weatherWritable>
            outKey.set(weatherWritable.getCode() + "-" + weatherWritable.getDate());
            context.write(outKey, weatherWritable);
        }
    }

    private static class DataCleanReducer extends Reducer<Text, WeatherWritable, WeatherWritable, NullWritable> {

        WeatherWritable.Builder builder = new WeatherWritable.Builder();

        @Override
        protected void reduce(Text key, Iterable<WeatherWritable> values, Reducer<Text, WeatherWritable, WeatherWritable, NullWritable>.Context context) throws IOException, InterruptedException {
            String code = null, date = null;
            float precipitation = 0.0F, maxTemp = 0.0F, minTemp = 0.0F, avgTemp = 0.0F;

            for (WeatherWritable value : values) {
                code = value.getCode();
                date = value.getDate();
                precipitation += value.getPrecipitation();
                maxTemp = maxTemp + value.getMaxTemperature();
                minTemp = minTemp + value.getMinTemperature();
                avgTemp = avgTemp + value.getAvgTemperature();
            }
            // 数据验证
            if (avgTemp > maxTemp || avgTemp < minTemp) {
                return;
            }

            WeatherWritable weatherWritable = builder
                    .setCode(code)
                    .setDate(date)
                    .setMaxTemperature(maxTemp)
                    .setMinTemperature(minTemp)
                    .setAvgTemperature(avgTemp)
                    .setPrecipitation(precipitation).build();
            context.write(weatherWritable, NullWritable.get());
        }
    }

}

```

#### `YearSummary.java`

```java
package club.kwcoder.weather.runner;

import club.kwcoder.weather.util.ValidateUtils;
import club.kwcoder.weather.writable.WeatherWritable;
import club.kwcoder.weather.writable.WeatherWritableSummary;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * 按年汇总数据，输出结果：观测站编码、年份、年降水量、年最高温度、年最低温度、降水天数
 */
public class YearSummary {

    public static class YearSummaryMapper extends Mapper<LongWritable, WeatherWritable, Text, WeatherWritableSummary> {

        private final Text outKey = new Text();
        private final WeatherWritableSummary.Builder outValBuilder = new WeatherWritableSummary.Builder();

        @Override
        protected void map(LongWritable key, WeatherWritable value, Mapper<LongWritable, WeatherWritable, Text, WeatherWritableSummary>.Context context) throws IOException, InterruptedException {
            WeatherWritableSummary outVal = outValBuilder
                    .setCode(value.getCode())
                    .setYear(value.getDate().split("/")[2])
                    .setPrecipitation(value.getPrecipitation())
                    .setMaxTemperature(value.getMaxTemperature())
                    .setMinTemperature(value.getMinTemperature())
                    .setRainDays(value.getPrecipitation() == 0.0F ? 0 : 1)
                    .setAvgTemperature(value.getAvgTemperature())
                    .buildSummary();
            outKey.set(outVal.getCode() + "-" + outVal.getYear());
            context.write(outKey, outVal);
        }

    }

    public static class YearSummaryReducer extends Reducer<Text, WeatherWritableSummary, WeatherWritable, NullWritable> {
        private final WeatherWritableSummary.Builder outKeyBuilder = new WeatherWritableSummary.Builder();
        private final NullWritable outVal = NullWritable.get();

        @Override
        protected void reduce(Text key, Iterable<WeatherWritableSummary> values, Reducer<Text, WeatherWritableSummary, WeatherWritable, NullWritable>.Context context) throws IOException, InterruptedException {
            String code = "", year = "";
            int rainDays = 0;
            float precipitation = 0.0F, maxTemp = Float.MIN_VALUE, minTemp = Float.MAX_VALUE, avgTemp = 0.0F;
            int avgTempNum = 0;
            // 计算年降雨量、年最高气温、年最低气温、降雨天数
            for (WeatherWritableSummary value : values) {
                if (ValidateUtils.validate(code) || ValidateUtils.validate(year)) {
                    code = value.getCode();
                    year = value.getYear();
                }
                precipitation += value.getPrecipitation();
                maxTemp = Math.max(maxTemp, value.getMaxTemperature());
                if (value.getMinTemperature() != 0.0F) {
                    minTemp = Math.min(minTemp, value.getMinTemperature());
                }
                rainDays += value.getRainDays();
                if (value.getAvgTemperature() != 0.0F) {
                    avgTemp += value.getAvgTemperature();
                    avgTempNum++;
                }
            }
            WeatherWritableSummary outKey = outKeyBuilder
                    .setCode(code)
                    .setYear(year)
                    .setPrecipitation(precipitation)
                    .setMaxTemperature(maxTemp)
                    .setMinTemperature(minTemp)
                    .setRainDays(rainDays)
                    .setAvgTemperature(avgTemp / avgTempNum)
                    .buildSummary();
            // 输出格式：code year precipitation maxTemperature minTemperature rainDays
            context.write(outKey, outVal);
        }
    }

}
```

#### `Forecast.java`

```java
package club.kwcoder.weather.runner;

import club.kwcoder.weather.writable.ForecastWritable;
import club.kwcoder.weather.writable.WeatherWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * 根据历史数据获取预测数据
 */
public class Forecast {

    public static class ForecastMapper extends Mapper<LongWritable, WeatherWritable, Text, WeatherWritable> {
        private final Text outKey = new Text();

        @Override
        protected void map(LongWritable key, WeatherWritable value, Mapper<LongWritable, WeatherWritable, Text, WeatherWritable>.Context context) throws IOException, InterruptedException {
            String[] split = value.getDate().split("/");
            String date = split[0] + "/" + split[1];
            outKey.set(date);
            context.write(outKey, value);
        }
    }

    public static class ForecastReducer extends Reducer<Text, WeatherWritable, ForecastWritable, NullWritable> {

        private final ForecastWritable.Builder outKeyBuilder = new ForecastWritable.Builder();
        private final NullWritable outVal = NullWritable.get();

        @Override
        protected void reduce(Text key, Iterable<WeatherWritable> values, Reducer<Text, WeatherWritable, ForecastWritable, NullWritable>.Context context) throws IOException, InterruptedException {
            float sumMaxTemp = 0.0F, sumMinTemp = 0.0F, sumAvgTemp = 0.0F, sumPrecipitation = 0.0F;
            int validMaxTemp = 0, validMinTemp = 0, validAvgTemp = 0, validPrecipitation = 0;
            for (WeatherWritable value : values) {
                if (value.getMaxTemperature() != 0.0F) {
                    sumMaxTemp += value.getMaxTemperature();
                    validMaxTemp++;
                }
                if (value.getMinTemperature() != 0.0F) {
                    sumMinTemp += value.getMinTemperature();
                    validMinTemp++;
                }
                if (value.getAvgTemperature() != 0.0F) {
                    sumAvgTemp += value.getAvgTemperature();
                    validAvgTemp++;
                }
                if (value.getPrecipitation() != 0.0F) {
                    sumPrecipitation += value.getPrecipitation();
                    validPrecipitation++;
                }
            }
            ForecastWritable outKey = outKeyBuilder
                    .setDate(key.toString())
                    .setMaxTemperature(sumMaxTemp / validMaxTemp)
                    .setMinTemperature(sumMinTemp / validMinTemp)
                    .setAvgTemperature(sumAvgTemp / validAvgTemp)
                    .setPrecipitation(validPrecipitation == 0 ? 0.0F : sumPrecipitation / validPrecipitation)
                    .build();
            System.out.println(outKey);
            context.write(outKey, outVal);
        }
    }

}

```

### 6、启动项代码编写`WeatherStarter.java`

```java
package club.kwcoder.weather;

import club.kwcoder.weather.writable.ForecastWritable;
import club.kwcoder.weather.writable.WeatherWritable;
import club.kwcoder.weather.runner.DataCleaning;
import club.kwcoder.weather.runner.Forecast;
import club.kwcoder.weather.runner.YearSummary;
import club.kwcoder.weather.util.HadoopUtils;
import club.kwcoder.weather.writable.WeatherWritableSummary;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.db.DBInputFormat;
import org.apache.hadoop.mapreduce.lib.db.DBOutputFormat;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * 启动类
 */
public class WeatherStarter {

    /**
     * 运行时参数，用于添加输入/输出路径
     */
    private static Map<String, String> TABLE;
    private static Map<String, String[]> FIELD;
    private static Map<String, Class<? extends DBWritable>> CLASS;

    public static final String MysqlDriverClass = "com.mysql.cj.jdbc.Driver";
    public static final String MysqlUrl = "jdbc:mysql://localhost:3306/weather";
    public static final String username = "root";
    public static final String password = "09140727";

    static {
        TABLE = new HashMap<>();
        FIELD = new HashMap<>();
        CLASS = new HashMap<>();

        TABLE.put("data_cleaning_output_table", WeatherWritable.tableName);
        FIELD.put("data_cleaning_output_field", WeatherWritable.fields);
        CLASS.put("data_cleaning_output_class", WeatherWritable.class);

        TABLE.put("year_summary_input_table", TABLE.get("data_cleaning_output_table"));
        FIELD.put("year_summary_input_field", FIELD.get("data_cleaning_output_field"));
        CLASS.put("year_summary_input_class", CLASS.get("data_cleaning_output_class"));

        TABLE.put("year_summary_output_table", WeatherWritableSummary.tableName);
        FIELD.put("year_summary_output_field", WeatherWritableSummary.fields);
        CLASS.put("year_summary_output_class", WeatherWritableSummary.class);


        TABLE.put("forecast_input_table", TABLE.get("data_cleaning_output_table"));
        FIELD.put("forecast_input_field", FIELD.get("data_cleaning_output_field"));
        CLASS.put("forecast_input_class", CLASS.get("data_cleaning_output_class"));

        TABLE.put("forecast_output_table", ForecastWritable.tableName);
        FIELD.put("forecast_output_field", ForecastWritable.fields);
        CLASS.put("forecast_output_class", ForecastWritable.class);

    }

    public static void main(String[] args) {
        DataCleaning.runner();
        run("year_summary", YearSummary.YearSummaryMapper.class, YearSummary.YearSummaryReducer.class, WeatherWritableSummary.class, WeatherWritable.class);
        run("forecast", Forecast.ForecastMapper.class, Forecast.ForecastReducer.class, WeatherWritable.class, ForecastWritable.class);
    }

    /**
     * 根据Runner接口的实现类，利用反射机制获取该类实例和run方法并调用
     */
    public static void run(String jobName,
                           Class<? extends Mapper<LongWritable, ? extends DBWritable, Text, ? extends DBWritable>> mapper,
                           Class<? extends Reducer<Text, ? extends DBWritable, ? extends DBWritable, NullWritable>> reducer,
                           Class<? extends DBWritable> mapOV, Class<? extends DBWritable> redOK) {
        Configuration conf = HadoopUtils.getConf();

        DBConfiguration.configureDB(
                conf,
                MysqlDriverClass,
                MysqlUrl,
                username,
                password
        );

        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName(MysqlDriverClass);
            connection = DriverManager.getConnection(MysqlUrl, username, password);
            statement = connection.createStatement();
            String sql = "delete from " + TABLE.get(jobName + "_output_table") + " where 1=1";
            statement.execute(sql);
            connection.commit();
        } catch (Exception ignore) {
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ignore) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ignore) {
                }
            }
        }

        try {
            Job job = Job.getInstance(conf, jobName);
            // 设置输入
            job.setInputFormatClass(DBInputFormat.class);
            DBInputFormat.setInput(
                    job,
                    CLASS.get(jobName + "_input_class"),
                    TABLE.get(jobName + "_input_table"),
                    null,
                    null,
                    FIELD.get(jobName + "_input_field")
            );
            // 设置Mapper
            job.setMapperClass(mapper);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(mapOV);
            // 设置Reducer
            job.setReducerClass(reducer);
            job.setOutputKeyClass(redOK);
            job.setOutputValueClass(NullWritable.class);
            // 设置输出
            job.setOutputFormatClass(DBOutputFormat.class);
            DBOutputFormat.setOutput(
                    job,
                    TABLE.get(jobName + "_output_table"),
                    FIELD.get(jobName + "_output_field")
            );
            // 运行
            boolean flag = job.waitForCompletion(true);
            if (flag) {
                System.out.println(jobName + " process success");
            }
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
```

### 6、应用端代码（NodeJs）

#### `package.json`

```json
{
  "name": "weather_app",
  "version": "1.0.0",
  "description": "",
  "author": "治怒善男 <1377875184@qq.com>",
  "private": true,
  "scripts": {
    "dev": "webpack-dev-server --inline --progress --config build/webpack.dev.conf.js",
    "start": "npm run dev",
    "build": "node build/build.js"
  },
  "dependencies": {
    "axios": "^0.27.2",
    "echarts": "^5.3.3",
    "element-ui": "^2.15.9",
    "express": "^4.18.1",
    "mysql": "^2.18.1",
    "vue": "^2.5.2",
    "vue-router": "^3.0.1"
  },
  "devDependencies": {
    "autoprefixer": "^7.1.2",
    "babel-core": "^6.22.1",
    "babel-helper-vue-jsx-merge-props": "^2.0.3",
    "babel-loader": "^7.1.1",
    "babel-plugin-syntax-jsx": "^6.18.0",
    "babel-plugin-transform-runtime": "^6.22.0",
    "babel-plugin-transform-vue-jsx": "^3.5.0",
    "babel-preset-env": "^1.3.2",
    "babel-preset-stage-2": "^6.22.0",
    "chalk": "^2.0.1",
    "copy-webpack-plugin": "^4.0.1",
    "css-loader": "^0.28.0",
    "extract-text-webpack-plugin": "^3.0.0",
    "file-loader": "^1.1.4",
    "friendly-errors-webpack-plugin": "^1.6.1",
    "html-webpack-plugin": "^2.30.1",
    "node-notifier": "^5.1.2",
    "optimize-css-assets-webpack-plugin": "^3.2.0",
    "ora": "^1.2.0",
    "portfinder": "^1.0.13",
    "postcss-import": "^11.0.0",
    "postcss-loader": "^2.0.8",
    "postcss-url": "^7.2.1",
    "rimraf": "^2.6.0",
    "semver": "^5.3.0",
    "shelljs": "^0.7.6",
    "uglifyjs-webpack-plugin": "^1.1.1",
    "url-loader": "^0.5.8",
    "vue-loader": "^13.3.0",
    "vue-style-loader": "^3.0.1",
    "vue-template-compiler": "^2.5.2",
    "webpack": "^3.6.0",
    "webpack-bundle-analyzer": "^2.9.0",
    "webpack-dev-server": "^2.9.1",
    "webpack-merge": "^4.1.0"
  },
  "engines": {
    "node": ">= 6.0.0",
    "npm": ">= 3.0.0"
  },
  "browserslist": [
    "> 1%",
    "last 2 versions",
    "not ie <= 8"
  ]
}
```

#### Node后端启动类`index.js`

```javascript
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

```

#### Node后端排序工具类`sort.js`

```javascript
let sortUtil = {
  sort: function (obj) {
    return function (src, tar) {
      //获取比较的值
      let v1 = src[obj];
      let v2 = tar[obj];
      if (v1 === 12 && v2 === 1) {
        return -1;
      }
      if (v1 > v2) {
        return 1;
      }
      if (v1 < v2) {
        return -1;
      }
      return 0;
    };
  }
}


exports.sort = sortUtil.sort

```

#### Node后端数据库配置`mysql.js`

```javascript
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

```



#### Node后端数据库访问`weather.js`

```javascript
module.exports = {
  query_by_date: "select * from weather where date like ?"
}
```

#### Node后端数据库访问`forecast.java`

```javascript
module.exports = {
  query_by_date: "select * from forecast where date in ?",
  query_all: "select * from forecast"
}
```

#### Node后端数据库访问`weather_year.js`

```javascript
module.exports = {
  query_by_year: "select * from weather_year where year=?",
  query_all: "select * from weather_year"
}
```

####  Node后端接口`forecast.java`

```javascript
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

```

#### Node后端接口`weather_api.js`

```javascript
// 测试用 api 实例
let express = require('express');
let router = express.Router();
let conn = require("../config/mysql").conn
let $weather_sql = require('../sql/weather');


// GET 请求
router.get('/get_history_weather', (req, res) => {
  let query = $weather_sql.query_by_date;

  let dayInYear = new Date()
  dayInYear = dayInYear.getDate().toString().padStart(2, "0") + "/" + (dayInYear.getMonth() + 1).toString().padStart(2, "0") + "/%"

  conn.query(query, [dayInYear], (err, result) => {

    let year = []
    let maxTemperature = []
    let minTemperature = []
    let avgTemperature = []
    let precipitation = []

    for (let item in result) {
      let data = result[item]
      if (data["maxTemperature"] === 0 || data["minTemperature"] === 0 || data["avgTemperature"] === 0) {
        continue
      }
      year.push(data["date"].split("/")[2])
      maxTemperature.push(data["maxTemperature"])
      minTemperature.push(data["minTemperature"])
      avgTemperature.push(data["avgTemperature"])
      precipitation.push(parseFloat(data["precipitation"]))
    }

    let option = {
      xAxis: {
        data: year,
        axisLabel: {
          textStyle: {
            color: "#ffffff"
          }
        }
      },
      yAxis: [
        {
          name: "温度(°C)",
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
          min: 0,
          max: (parseInt(precipitation.sort().reverse()[0] / 20) + 1) * 20,
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

module.exports = router;

```

#### Node后端接口`weather_year_api.js`

```javascript
let express = require('express');
const $weather_year_api = require("../sql/weather_year");
const {sort} = require("../util/sort");
const conn = require("../config/mysql").conn
let router = express.Router();

router.get('/get_all_year', (req, res) => {
  let query = $weather_year_api.query_all;
  conn.query(query, (err, result) => {

    result.sort(sort("year"))

    let year = []
    let maxTemperature = []
    let minTemperature = []
    let avgTemperature = []
    let precipitation = []
    let rainDays = []

    for (let item in result) {
      year.push(result[item]["year"])
      maxTemperature.push(result[item]["maxTemperature"])
      minTemperature.push(result[item]["minTemperature"])
      avgTemperature.push(result[item]["avgTemperature"])
      precipitation.push(result[item]["precipitation"])
      rainDays.push(result[item]["rainDays"])
    }

    let option = {
      xAxis: {
        data: year,
        axisLabel: {
          textStyle: {
            color: "#ffffff"
          }
        }
      },
      yAxis: [
        {
          name: "温度(°C)",
          splitNumber: 4,
          min: 0,
          max: 40,
          axisLabel: {
            textStyle: {
              color: "#ffffff"
            }
          }
        },
        {
          name: "年降水量(mm)",
          splitNumber: 4,
          min: 0,
          max: 2000,
          axisLabel: {
            textStyle: {
              color: "#ffffff"
            }
          }
        },
        {
          name: "年降雨天数",
          max: 200,
          min: 0,
          splitNumber: 4,
          show: false
        }
      ],
      series: [
        {
          name: "年最高温度",
          type: "line",
          smooth: true,
          symbol: "none",
          itemStyle: {
            normal: {
              lineStyle: {
                color: "#FF0000"
              }
            }
          },
          data: maxTemperature,

        },
        {
          name: "年最低温度",
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
          name: "年平均温度",
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
          name: "年降水量",
          data: precipitation,
          type: "bar",
          yAxisIndex: 1,
          itemStyle: {
            normal: {
                color: "#00CCFF"
            }
          },
        },
        {
          name: "年降雨天数",
          data: rainDays,
          type: "bar",
          yAxisIndex: 2,
          itemStyle: {
            normal: {
              color: "#FFFFCC"
            }
          }
        }
      ]
    }


    res.json(option)
  });
})

module.exports = router;

```

#### Vue——`main.js`

```javascript
// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import * as echarts from 'echarts'
import axios from "axios";

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

Vue.config.productionTip = false
Vue.prototype.$echarts = echarts


axios.defaults.baseURL = "http://localhost:8888/api/"
Vue.prototype.$axios = axios

Vue.use(ElementUI);

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})

```

#### Vue——`App.vue`

```vue
<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'PingFangSC-Regular', 'helvetica neue', tahoma, 'PingFang SC', 'microsoft yahei', arial, 'hiragino sans gb', sans-serif;
  overflow: hidden;
}
</style>

```

#### Vue——`index.js`（路由）

```javascript
import Vue from 'vue'
import Router from 'vue-router'
import weather from "../components/weather";

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'weather',
      component: weather
    }
  ]
})

```



#### Vue——`weather.vue`

```vue
<template>

  <div id="weather_app">

    <div class="head">
      <h1>19数据课程报告</h1>
    </div>

    <div class="container">

      <div style="margin: 1.25rem 1.25rem auto 1.25rem">

        <div class="tail_box all_box box_head" style="float:left;">
          <div ref="report" style="height: 40vh; width: 100%;"></div>
        </div>
        <div class="tail_box all_box box_head" style="float:right;">
          <div ref="history" style="height: 40vh; width: 100%;"></div>
        </div>

      </div>

      <div style="margin: 1.25rem 1.25rem auto 1.25rem">
        <div class="tail_box all_box box_head" style="float:left;">
          <div ref="forecast" style="height: 40vh; width: 100%;"></div>
        </div>
        <div class="tail_box all_box box_head" style="float:right;">
          <div ref="temperature" style="height: 40vh; width: 100%;"></div>
        </div>
      </div>

    </div>

  </div>

</template>

<script>
export default {
  name: 'weather',
  data() {
    return {
      report: "",
      history: "",
      temperature: '',
      forecast: '',
    }
  },
  async mounted() {
    this.initDom()
    this.reportChart()
    this.historyChart()
    this.temperatureChart()
    this.forecastChart()
  },
  methods: {
    reportChart() {
      let _this = this
      let chart = this.$echarts.init(_this.report)
      window.addEventListener("resize", function () {
        chart.resize();
      })
      _this.$axios.get("forecast").then((resp) => {
        let option = resp.data
        option["tooltip"] = {
          trigger: 'axis',
          show: true
        }
        option["title"] = {
          text: "一周天气预报",
          x: "center",
          y: "bottom",
          textStyle: {
            fontSize: 14,
            fontWeight: "normal",
            color: "#ffffff"
          }
        }
        option["color"] = ["#FF0000", "#00FF00", "#FF9900", "#00CCFF", "#FFFFCC"]
        chart.setOption(option)
      })
    },
    historyChart() {
      let _this = this
      let chart = this.$echarts.init(_this.history)
      window.addEventListener("resize", function () {
        chart.resize();
      })
      _this.$axios.get("get_history_weather").then((resp) => {
        let option = resp.data
        option["tooltip"] = {
          trigger: 'axis',
          show: true
        }
        let toDay = new Date()
        option["title"] = {
          text: "历史上的今天（" + toDay.getFullYear() + "-" + (toDay.getMonth() + 1).toString().padStart(2, "0") + "-" + (toDay.getDate()) + "）",
          x: "center",
          y: "bottom",
          textStyle: {
            fontSize: 14,
            fontWeight: "normal",
            color: "#ffffff"
          }
        }
        option["color"] = ["#FF0000", "#00FF00", "#FF9900", "#00CCFF", "#FFFFCC"]
        chart.setOption(option)
      })
    },
    temperatureChart() {
      let _this = this
      let chart = this.$echarts.init(_this.temperature)
      window.addEventListener("resize", function () {
        chart.resize();
      })
      // 绘制图表
      _this.$axios.get("get_all_year").then((resp) => {
        let option = resp.data
        option["dataZoom"] = [
          {
            type: "slider",
            realtime: true,
            height: 10,
            start: 0,
            end: 100,
            min: 10
          }
        ]
        option["tooltip"] = {
          trigger: 'axis',
          show: true
        }
        option["title"] = {
          text: "年最高、最低、平均气温",
          x: "center",
          y: "bottom",
          textStyle: {
            fontSize: 14,
            fontWeight: "normal",
            color: "#ffffff"
          }
        }
        option["color"] = ["#FF0000", "#00FF00", "#FF9900", "#00CCFF", "#FFFFCC"]
        chart.setOption(option)
      })
    },
    forecastChart() {
      let _this = this
      let chart = this.$echarts.init(_this.forecast)
      window.addEventListener("resize", function () {
        chart.resize();
      })
      // 绘制图表
      _this.$axios.get("get_all_forecast").then((resp) => {
        let option = resp.data
        option["tooltip"] = {
          trigger: 'axis',
          show: true
        }
        option["title"] = {
          text: "天气走向曲线",
          x: "center",
          y: "bottom",
          textStyle: {
            fontSize: 14,
            fontWeight: "normal",
            color: "#ffffff"
          }
        }
        option["color"] = ["#FF0000", "#00FF00", "#FF9900", "#00CCFF"]
        chart.setOption(option)
      })
    },
    initDom() {
      this.report = this.$refs.report
      this.history = this.$refs.history
      this.temperature = this.$refs.temperature
      this.forecast = this.$refs.forecast
    },
  }
}
</script>

<style scoped>

#weather_app {
  background: #000d4a url("../assets/background.jpg") center top;
  background-size: cover;
  color: #666;
  font-family: "微软雅黑", serif;
  height: 100vh;
  font-size: 1rem;
}

.head {
  height: 8vh;
  width: 100%;
  background: url("../assets/head_bg.png") no-repeat center center;
  background-size: 100% 100%;
}

h1 {
  text-align: center;
  color: rgba(255, 255, 255);
  font-weight: bold;
  line-height: 8vh;
}

.top_box {
  height: 50%;
  width: 96%;
  margin: 1.25rem auto auto;
}

.tail_box {
  height: 50%;
  width: 49%;
  float: left;
}

.all_box {
  border: .0625rem solid rgba(25, 186, 139, .17);
  padding: 0rem .2rem .4rem .15rem;
  background: rgba(255, 255, 255, .04) url("../assets/line.png");
  background-size: 100% auto;
  position: relative;
  margin-bottom: .15rem;
  z-index: 10;
}

.box_head::after {
  position: absolute;
  width: 0.5rem;
  height: 0.5rem;
  content: "";
  border-top: 0.125rem solid #02a6b5;
  border-right: 0.125rem solid #02a6b5;
  top: 0;
  right: 0;
}

.box_foot::before {
  position: absolute;
  width: 0.5rem;
  height: 0.5rem;
  content: "";
  border-bottom: 0.125rem solid #02a6b5;
  border-left: 0.125rem solid #02a6b5;
  bottom: 0;
  left: 0;
}

.title {
  height: 4.3vh;
  font-size: 1rem;
  color: white;
  text-align: center;
  line-height: 4.3vh;
}

</style>

```

####  图片资源

`background.jpg`

![background.jpg](https://picgo.kwcoder.club/202208/202207251118381.png)

`head_bg.png`

![head_bg.png](https://picgo.kwcoder.club/202208/202207251118170.png)

`line.png`

![](https://picgo.kwcoder.club/202208/202207251118550.png)





## 五、总结与期望

