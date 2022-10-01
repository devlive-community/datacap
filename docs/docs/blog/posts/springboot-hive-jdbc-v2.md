---
date: 2022-09-12
authors: [qianmoQ]
description: >
    Plugin插件配置Hive JDBC 2.x版本maven依赖
categories:
    - Spring Boot
    - Plugin
---

# Spring Boot使用hive-jdbc依赖包冲突

SpringBoot 版本不管多少，只要引入了hive-jdbc，指定会出现jar包冲突，不是内嵌tomcat问题，就是和tomcat jar包冲突。

为了方便使用，以下是一个配置hive-jdbc 2.x版本的maven配置。

```xml
<dependency>
    <groupId>org.apache.hive</groupId>
    <artifactId>hive-jdbc</artifactId>
    <version>${hive.version}</version>
    <exclusions>
        <exclusion>
            <groupId>org.eclipse.jetty.aggregate</groupId>
            <artifactId>jetty-all</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.apache.hive</groupId>
            <artifactId>hive-shims</artifactId>
        </exclusion>
        <exclusion>
            <artifactId>jasper-compiler</artifactId>
            <groupId>tomcat</groupId>
        </exclusion>
        <exclusion>
            <artifactId>jasper-runtime</artifactId>
            <groupId>tomcat</groupId>
        </exclusion>
        <exclusion>
            <artifactId>servlet-api</artifactId>
            <groupId>javax.servlet</groupId>
        </exclusion>
        <exclusion>
            <artifactId>log4j-slf4j-impl</artifactId>
            <groupId>org.apache.logging.log4j</groupId>
        </exclusion>
        <exclusion>
            <artifactId>slf4j-log4j12</artifactId>
            <groupId>org.slf4j</groupId>
        </exclusion>
        <exclusion>
            <groupId>tomcat</groupId>
            <artifactId>*</artifactId>
        </exclusion>
        <exclusion>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.eclipse.jetty.orbit</groupId>
            <artifactId>*</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.eclipse.jetty.aggregate</groupId>
            <artifactId>*</artifactId>
        </exclusion>
        <exclusion>
            <groupId>javax.servlet</groupId>
            <artifactId>servlet-api</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.mortbay.jetty</groupId>
            <artifactId>*</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

通过以上配置我们在SpringBoot中就可以正常使用hive-jdbc 2.x的版本了。
