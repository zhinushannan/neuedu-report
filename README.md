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

![image-20220719161007112](https://pic-go-zhinushannan.oss-cn-hangzhou.aliyuncs.com/202206/202207191610723.png)

## 三、功能设计

### 1、模拟天气预测

模拟预测天气预报，生成一周的天气预测结果并进行可视化展示。

![image-20220719162244043](https://pic-go-zhinushannan.oss-cn-hangzhou.aliyuncs.com/202206/202207191622401.png)

### 2、历史天气查询

展示历史上今天的天气数据。

![image-20220719162321230](https://pic-go-zhinushannan.oss-cn-hangzhou.aliyuncs.com/202206/202207191623496.png)

### 3、天气走向曲线

根据历史数据，计算出每年1-12月份每月气温和降水的趋势。

![image-20220719162357912](https://pic-go-zhinushannan.oss-cn-hangzhou.aliyuncs.com/202206/202207191623131.png)

### 4、历史年份数据查询

通过“拖拉拽”的方式在`年最高、最低、平均温度`模块中，可以查看每年的最高气温、最低气温、平均气温、年总降水量和年总降水天数。

![image-20220719161516639](https://pic-go-zhinushannan.oss-cn-hangzhou.aliyuncs.com/202206/202207191615207.png)

### 5、页面自适应

页面可以根据窗口大小的动态变化主动实时调整图例大小。

![image-20220719162016209](https://pic-go-zhinushannan.oss-cn-hangzhou.aliyuncs.com/202206/202207191620968.png)

![image-20220719162041710](https://pic-go-zhinushannan.oss-cn-hangzhou.aliyuncs.com/202206/202207191620179.png)

## 四、详细设计

大数据处理流程主要包括数据收集、数据预处理、数据存储、数据处理与分析、数据展示/数据可视化、数据应用等环节，其中数据质量贯穿于整个大数据流程，每一个数据处理环节都会对大数据质量产生影响作用。

通常，一个好的大数据产品要有大量的数据规模、快速的数据处理、精确的数据分析与预测、优秀的可视化图表以及简练易懂的结果解释。

### 1、数据导入

#### 准备数据集：

数据集来源：[https://www.kaggle.com/saraivaufc/conventional-weather-stations-brazil#conventional_weather_stations_inmet_brazil_1961_2019.csv](https://www.kaggle.com/saraivaufc/conventional-weather-stations-brazil#conventional_weather_stations_inmet_brazil_1961_2019.csv)

或通过`Release`下载：[https://github.com/zhinushannan/weather/releases/tag/data](https://github.com/zhinushannan/weather/releases/tag/data)

#### 数据导入：

通过HDFS的WEB端上传数据

![image-20220719164105691](https://pic-go-zhinushannan.oss-cn-hangzhou.aliyuncs.com/202206/202207191641858.png)

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

## 五、总结与期望

