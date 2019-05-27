# Spring Data JPA集成H2内存数据库

## 集成H2数据库
h2数据库是常用的开源数据库，与HSQLDB类似，十分适合作为嵌入式数据库使用，其他的数据库大部分都需要安装独立的客户端和服务器端。

h2的优势：
1. h2采用纯java编写，因此不受平台的限制
2. h2只有一个jar文件，十分适合作为嵌入式数据库使用
3. h2提供了一个十分方便的web控制台用于操作和管理数据库内容。

### 添加依赖
bulid.gradle:
```groovy
compile 'org.springframework.boot:spring-boot-starter-data-jpa'
compile 'org.springframework.boot:spring-boot-starter-web'
runtime 'com.h2database:h2'
```
'spring-boot-starter-data-jpa'应该不是必须的，但不加入该依赖，会导致无法进入'h2 web consloe'控制台，原因暂不明。

### application.yml配置
```groovy
spring:
  datasource:
    url: jdbc:h2:mem:h2test; #配置h2数据库的连接地址
    platform: h2 #表明使用的数据库平台是h2
    username: sa #配置数据库用户名
    password: #配置数据库密码
    driverClassName: org.h2.Driver #配置JDBC Driver
    schema: classpath:db/schema.sql #进行该配置后，每次启动程序，程序都会运行resources/db/schema.sql文件，对数据库的结构进行操作
    data: classpath:db/data.sql #进行该配置后，每次启动程序，程序都会运行resources/db/data.sql文件，对数据库的数据操作
  h2:
    console:
      enabled: true #进行该配置，程序开启时就会启动h2 web consloe
      path: /h2-console #可以通过YOUR_URL/h2-console来访问'数据库GUI管理：h2 web consloe'。YOUR_URL是你程序的访问URl。
      settings:
        trace: false
        web-allow-others: true #进行该配置后，h2 web consloe就可以在远程访问了。否则只能在本机访问
logging:
  level: debug
```

### 编写建表语句
- 参见：resources/db/data.sql 及 resources/db/schema.sql 的内容

### 启动工程，访问 h2 web consloe
![这里随便写文字](你刚复制的图片路径)
1. 输入地址：'http://127.0.0.1:8080/h2-console/'
2. 'Saved Settings'和'Setting Name'选择：Generic H2 (Server)
3. JDBC URL填写：jdbc:h2:mem:h2test。(与application配置文件中的'spring.datasource.url'保持一致)


## Refer
[Spring Data JPA(二)：SpringBoot集成H2](https://niocoder.com/2018/03/23/Spring-Data-JPA(二)-SpringBoot集成H2/#applicationyml)