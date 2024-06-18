# 项目结构

    │  docker-compose.env.yml     //（mysql，redis，rabbitmq，nacos，sentinel）
    │  docker-compose.service.yml //服务模块
    │  pom.xml
    │
    ├─memo-api //远程调用模块
    │  │  pom.xml
    │  │
    │  └─src
    │      ├─main
    │      │  ├─java
    │      │  │  └─com
    │      │  │      └─memo
    │      │  │          └─api
    │      │  │              ├─client
    │      │  │              │      UserClient.java
    │      │  │              │
    │      │  │              └─config
    │      │  │                      DefaultFeignConfig.java //openfeign传递用户信息
    │      │  │
    │      │  └─resources
    │      └─test
    │          └─java
    ├─memo-common //通用模块
    │  │  pom.xml
    │  │
    │  └─src
    │      ├─main
    │      │  ├─java
    │      │  │  └─com
    │      │  │      └─memo
    │      │  │          └─common
    │      │  │              ├─config
    │      │  │              │      MvcConfig.java
    │      │  │              │
    │      │  │              ├─interceptor
    │      │  │              │      UserInfoInterceptor.java //获取用户信息
    │      │  │              │
    │      │  │              ├─pojo
    │      │  │              │      Result.java
    │      │  │              │
    │      │  │              └─util
    │      │  │                      JwtUtil.java
    │      │  │                      Md5Util.java
    │      │  │                      SnowFlakeUtil.java
    │      │  │                      ThreadLocalUtil.java
    │      │  │
    │      │  └─resources
    │      │      └─META-INF
    │      │              spring.factories
    │      │
    │      └─test
    │          └─java
    ├─memo-gateway //网关
    │  │  Dockerfile
    │  │  pom.xml
    │  │
    │  └─src
    │      ├─main
    │      │  ├─java
    │      │  │  └─com
    │      │  │      └─memo
    │      │  │          └─gateway
    │      │  │              │  GatewayApplication.java
    │      │  │              │
    │      │  │              ├─config
    │      │  │              │      AuthProperties.java
    │      │  │              │
    │      │  │              ├─filters
    │      │  │              │      AuthGlobalFilter.java //过滤器
    │      │  │              │
    │      │  │              └─util
    │      │  │                      JwtUtil.java
    │      │  │
    │      │  └─resources
    │      │          application-prod.yml
    │      │          application.yml
    │      │
    │      └─test
    │          └─java
    ├─mysql-init //建表脚本
    │      create_table.sql
    │
    ├─transaction-service //事务模块
    │  │  Dockerfile
    │  │  pom.xml
    │  │
    │  └─src
    │      ├─main
    │      │  ├─java
    │      │  │  └─com
    │      │  │      └─memo
    │      │  │          └─transaction
    │      │  │              │  TransactionApplication.java
    │      │  │              │
    │      │  │              ├─config
    │      │  │              │      ExceptionHandler.java
    │      │  │              │      RedisConfig.java
    │      │  │              │
    │      │  │              ├─controller
    │      │  │              │      ThingController.java
    │      │  │              │
    │      │  │              ├─listener
    │      │  │              │      RabbitMqListener.java //redis删除重试
    │      │  │              │
    │      │  │              ├─mapper
    │      │  │              │      ThingMapper.java
    │      │  │              │
    │      │  │              ├─pojo
    │      │  │              │      CacheMessage.java
    │      │  │              │      PageBean.java
    │      │  │              │      Thing.java
    │      │  │              │
    │      │  │              └─service
    │      │  │                  │  ThingService.java
    │      │  │                  │
    │      │  │                  └─Impl
    │      │  │                          ThingServiceImpl.java
    │      │  │
    │      │  └─resources
    │      │          application-prod.yml
    │      │          application.yml
    │      │
    │      └─test
    │          └─java
    └─user-service //用户模块
        │  Dockerfile
        │  pom.xml
        │
        └─src
            ├─main
            │  ├─java
            │  │  └─com
            │  │      └─memo
            │  │          └─user
            │  │              │  UserApplication.java
            │  │              │
            │  │              ├─controller
            │  │              │      UserController.java
            │  │              │
            │  │              ├─mapper
            │  │              │      UserMapper.java
            │  │              │
            │  │              ├─pojo
            │  │              │      User.java
            │  │              │
            │  │              └─service
            │  │                  │  UserService.java
            │  │                  │
            │  │                  └─Impl
            │  │                          UserServiceImpl.java
            │  │
            │  └─resources
            │      │  application-prod.yml
            │      │  application.yml
            │      │
            │      └─mybatis.mapper
            │              UserMapper.xml
            │
            └─test
                └─java
