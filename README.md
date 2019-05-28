# Spring Data JPA集成H2内存数据库

## 一、集成H2数据库
h2数据库是常用的开源数据库，与HSQLDB类似，十分适合作为嵌入式数据库使用，其他的数据库大部分都需要安装独立的客户端和服务器端。

h2的优势：
1. h2采用纯java编写，因此不受平台的限制
2. h2只有一个jar文件，十分适合作为嵌入式数据库使用，可以同应用程序打包在一起发布
3. h2提供了一个十分方便的web控制台用于操作和管理数据库内容。

### 1、添加依赖
bulid.gradle:
```groovy
compile 'org.springframework.boot:spring-boot-starter-data-jpa'
compile 'org.springframework.boot:spring-boot-starter-web'
runtime 'com.h2database:h2'
```
'spring-boot-starter-data-jpa'应该不是必须的，但不加入该依赖，会导致无法进入'h2 web consloe'控制台，原因暂不明。

### 2、application.yml配置
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

### 3、编写建表语句
- 参见：resources/db/data.sql 及 resources/db/schema.sql 的内容

### 4、启动工程，访问 h2 web consloe
1. 输入地址：'http://127.0.0.1:8080/h2-console/'
2. 'Saved Settings'和'Setting Name'选择：Generic H2 (Server)
3. JDBC URL填写：jdbc:h2:mem:h2test。(与application配置文件中的'spring.datasource.url'保持一致)

![登录界面](https://github.com/ningjia/h2db/blob/master/imgs/H2WebConsole01.png)
![控制台界面1](https://github.com/ningjia/h2db/blob/master/imgs/H2WebConsole02.png)
![控制台界面2](https://github.com/ningjia/h2db/blob/master/imgs/H2WebConsole03.png)

## 二、集成JPA
### 1、添加依赖
```groovy
compile 'org.projectlombok:lombok'
```
### 2、application.yml中增加JPA配置
```groovy
jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        show_sql: true #启用SQL语句的日志记录
        USER_sql_comments: true
        format_sql: true
```
### 3、增加实体类
```java
@Entity
@Table(name = "t_user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column(name="user_name") //由于在使用JPA的findby方法时，名称中不能出现下划线，所以在此处将名称中的下划线去掉
    private String userName;

    @Column(name="user_age")
    private Integer userAge;

    @Column(name="create_time")
    private Date createTime;

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", userName='" + userName + '\'' +
                ", userAge=" + userAge +
                ", createTime=" + createTime +
                '}';
    }
}
```
- @Table声明此对象映射到数据库的数据表，通过它可以为实体指定表(talbe),目录(Catalog)和schema的名字。该注释不是必须的，如果没有则系统使用默认值(实体的短类名)。
- @Id 声明此属性为主键。该属性值可以通过应该自身创建，但是Hibernate推荐通过Hibernate生成
- @GeneratedValue 指定主键的生成策略。
    - TABLE：使用表保存id值
    - IDENTITY：identitycolumn
    - SEQUENCR ：sequence
    - AUTO：根据数据库的不同使用上面三个
- @Column 声明该属性与数据库字段的映射关系。

### 3、增加Repository类
```java
public interface UserRepository extends JpaRepository<User, Integer> {
    //自定义方法，按照userName查询用户数据
    List<User> findByUserName(String user_name);
}
```

### 4、测试类
```java
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findUserTest() throws Exception{
        //插入测试数据
        User user = new User();
        user.setUserName("测试用户");
        user.setUserAge(20);
        user.setCreateTime(new Date());
        User result = userRepository.save(user);
        Assert.assertNotNull(user.getUser_id());
        log.info(result.toString());
        //查找用户数据
        List<User> users = userRepository.findByUserName("测试用户");
        Assert.assertTrue(users.size()>0);
        Assert.assertTrue(20==users.get(0).getUserAge());
        log.info(users.get(0).toString());
    }
}
```

## H2数据库的持久化
修改application.yml配置文件中的spring.datasource.url属性，设置为持久化时所对应的本地文件路径。
```groovy
url: jdbc:h2:file:/Users/h2database;
```
- 启动后，会自动在/Users目录下，创建'h2database.xx.db'文件。

## 三、Refer
[Spring Data JPA(二)：SpringBoot集成H2](https://niocoder.com/2018/03/23/Spring-Data-JPA(二)-SpringBoot集成H2/#applicationyml)
