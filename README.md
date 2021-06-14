# 动态多数据源并自动集成 Druid，Mybatis，MybatisPlus

## 简介

本动态多数据源实现参考[ `baomidou/dynamic-datasource-spring-boot-starter`](https://gitee.com/baomidou/dynamic-datasource-spring-boot-starter)实现，通过自定义切面切点的方式实现动态数据源的切换，而不用使用大量的注解进行切换。通过实现自定义数据源路由规则的方式，完成更加灵活的数据源切换。

默认集成Druid连接池，Mybatis以及MybatisPlus，而无需进行其他的额外配置。

 可实现本地事务，但不可在事务中切换数据源。

## 配置

完成在yml中的数据源配置

在 `application.yml`中配置多种数据源信息：

```yml
spring:
  datasource:
    # 默认Druid的配置信息
    # Druid监控配置 WebStatFilter配置，说明请参考Druid Wiki，配置_配置WebStatFilter
    druid:
      filters: stat,wall
      web-stat-filter:
        enabled: true
        url-pattern: /*
        exclusions: /druid/*,*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico
        session-stat-enable: true
        session-stat-max-count: 10
        principal-session-name: session_name
        principal-cookie-name: cookie_name
        # StatViewServlet配置，说明请参考Druid Wiki，配置_StatViewServlet配置默认false
      stat-view-servlet:
        enabled: true
        # 配置DruidStatViewServlet
        url-pattern: /druid/*
        #  禁用HTML页面上的“Reset All”功能
        reset-enable: false
        #监控页面登录的用户名
        login-username: admin
        #监控页面登录的密码
        login-password: 123456
        
    # 默认数据源信息， 下面四个属性为必填项
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://{ip}:{port}/{database}?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8
    username: ***
    password: ***
    
    # 其他动态数据源信息
    dynamic:
      # 是否开启动态数据源
      enable: true
      # 需要进行切换数据源执行方法的切面表达式
      pointcut: execution(* com.tz.*.*(..))
      # 下面的例子 test1 为数据源的 key
      test1:
        # 下面四个属性为必填项
        driver-class-name: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://{ip}:{port}/test?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8
        username: ***
        password: ***
        # 可配置该数据源特殊的Druid连接池信息，不配置则获取默认Druid配置
        druid: 
          filters: # ...
```

需要额外关注的配置信息为：

- `spring.datasource.dynamic.enable`：设置是否启用动态数据源

- `spring.datasource.dynamic.pointcut`：设置切换数据源的方法切点表达式，动态数据源会在此切点前进行切换数据源。

- `spring.datasource.druid`：设置默认的Druid连接池配置信息，也可理解为公共的Druid连接池配置，如果其他的数据源不设置Druid信息，则会自动获取该配置进行设置。

- 默认数据源配置

  -   `spring.datasource.driver-class-name`：数据源驱动ClassName，必填
  -   `spring.datasource.username`：数据源用户名，必填
  -   `spring.datasource.password`：数据源密码，必填
  -   `spring.datasource.url`：数据源连接地址，必填

  以上配置如果一个未进行配置，则该数据源将不会被成功加载。

- `spring.datasource.dynamic`：其他数据源

  -   以map形式配置，即 `spring.datasource.dynamic.{datasource-key}`，key即为该数据源的名称
  -   必填配置项
      -   `driver-class-name`：数据源驱动ClassName
      -   `username`：数据源用户名
      -   `password`：数据源密码
      -   `url`：数据源连接地址
  -   可选配置
      -   `druid`：即 `spring.datasource.dynamic.{datasource-key}.druid`，可特殊配置该数据源的连接池设置

## 自动数据源切换

如需实现动态自动切换数据源，需完成一下两步：

1. 完成配置 `spring.datasource.dynamic.pointcut`切换数据源切点数据

2. 实现 `com.tz.dynamic.datasource.DynamicDatasourceRoute` 动态数据源路由接口。

   该接口只存在一个方法：`String getDatasourceRouteKey()`，可在该方法内部实现对于数据源动态切换的逻辑。

3.  **注意：该实现类需要使用 `@Service` 将其标记为SpringBean。**

## 数据源配置获取

完成了可从其他位置（比如数据库）获取数据源的配置信息，可通过以下几个步骤完成：

1. 实现 `com.tz.datasource.dynamic.config.reader.DataSourceConfigurationReader`数据源配置读取接口

   ```java
   Map<Object, DataSourceProperty> getDynamicDataSources();
   ```

   该接口返回动态数据源map，

   - 其key为该数据源的name（通过该值进行数据库切换）；
   - value则为 `com.tz.datasource.dynamic.config.DataSourceProperty`，其中 `url`，`username`，`password`，`driverClassName`不可为空

2. **将该实现使用 `@Service` 标记为SpringBean。**

该数据源配置获取机制会在MybatisPlus加载后再执行，因此可使用MybatisPlus从默认数据源（在`application.yml`中配置的`spring.datasource`数据源）获取到其他的数据源



## 手动数据源切换

使用 `com.tz.datasource.dynamic.core.DataSourceSelector`进行数据源的切换操作，提供以下两个方法：

- 选择数据源

  ```java
  void selectDataSource(String dataSourceKey)
  ```

  - dataSourceKey：数据源的name

- 选择默认数据源

  ```java
  void selectDefaultDataSource()
  ```

## 数据源的动态增删

使用 `com.tz.datasource.dynamic.core.DynamicDataSourceManager` 进行数据源的动态增加和删除。

-  添加数据源
  
  ```java
  void addDataSource(Object name, DataSource dataSource)
  ```
  
  - name：数据源的name，用于数据源切换
  - DataSource：数据源
  
- 删除数据源
  
  ```java
  void removeDataSource(Object name)
  ```
  
  -  name：数据源的name

