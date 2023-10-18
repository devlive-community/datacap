---
title: 在自主机中部署
---

DataCap是一款用于数据转换，集成和可视化的软件。

#### 系统要求

---

!!! warning

    该软件的二进制包基于以下系统进行编译和测试。它尚未在其他版本上进行测试，理论上受支持。

    如果有不支持的系统，请使用源码编译方法主动编译二进制文件。

| 系统    | 版本        |
|-------|-----------|
| JDK   | `>=11`    |
| MySQL | `>=5.6.x` |

#### 二进制安装

---

!!! note

    从以下地址下载相应系统的二进制软件包进行安装。

- [下载最新发布版本](/download.html)

##### 下载包

将二进制文件下载到本地后运行以下命令

```bash
tar -xvzf datacap-release.tar.gz
```

##### 软件配置

对于软件的首次安装，您需要将 `schema/datacap.sql` 文件中的sql脚本导入MySQL服务器。注意需要导入的脚本根据下载的软件包进行匹配

导入 `SQL` 脚本后，修改 `configure/application.properties` 配置文件以修改MySQL服务器的配置信息

添加配置

```bash
spring.datasource.url=jdbc:mysql://localhost:3306/datacap?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&useSSL=false&useOldAliasMetadataBehavior=true&jdbcCompliantTruncation=false&sessionVariables=sql_mode='STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION,PIPES_AS_CONCAT'
spring.datasource.username=root
spring.datasource.password=12345678
```

!!! warning

    如果需要修改日志配置，只需修改 `configure/logback.xml` 配置文件即可

##### 启动服务

DataCap服务启动非常简单，执行以下脚本

```bash
./bin/startup.sh
```

##### 停止服务

停止服务并执行以下脚本

```bash
./bin/shutdown.sh
```

!!! note

    如果要调试系统，可以使用 `./bin/debug.sh` 启动服务，但关闭窗口时它将停止

#### 源安装

---

!!! warning

    要手动编译并安装 DataCap，您需要执行以下步骤。

    系统需要安装 `JDK`

- 将源代码克隆到此计算机

```bash
git clone https://github.com/devlive-community/datacap.git
```

- 编译和构建应用程序

```bash
./mvnw clean install package -DskipTests -Dgpg.skip
```

!!! warning

    编译完成后，`datacap-release.tar.gz` 包会在 `dist` 目录中生成。

    使用相关软件包进行安装。

!!! note

    如果不想安装到本地软件目录，可以使用以下文档启动开发模式进行软件使用。

[开发者](../../developer_guide/env.md)
