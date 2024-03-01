---
title: Docker Compose 部署
---

DataCap 项目提供 Docker Compose 方式部署，通过下载 [docker-compose.yml](https://github.com/devlive-community/datacap/blob/dev/docker-compose.yml) 文件，或者使用以下代码进行服务部署。

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:latest
    environment:
      MYSQL_ROOT_PASSWORD: 12345678
      MYSQL_DATABASE: datacap
    ports:
      - "3306:3306"
    volumes:
      - ./core/datacap-server/src/main/schema/datacap.sql:/docker-entrypoint-initdb.d/schema.sql

  datacap:
    image: qianmoq/datacap:latest
    restart: always
    ports:
      - "9099:9099"
    depends_on:
      - mysql
    volumes:
      - ./configure/docker/application.properties:/opt/app/datacap/configure/application.properties

```

!!! warning

    需要同时下载一下多个文件：

    - [datacap.sql](https://github.com/devlive-community/datacap/blob/dev/core/datacap-server/src/main/schema/datacap.sql)
    - [application.properties](https://github.com/devlive-community/datacap/blob/dev/configure/docker/application.properties)

    下载完成后将他们放置到指定目录，也就是 `./configure/docker/` 和 `./core/datacap-server/src/main/schema/` 如果需要自定义目录，可以修改 `docker-compose.yml` 文件中挂载的 `volumes` 配置即可。

## 启动服务

---

以上工作完成后，使用以下命令进行启动服务。**必须在包含 docker-compose.yml 文件的目录下执行**

```bash
docker-compose up
```

如果需要后台启动使用以下命令

```bash
docker-compose up -d
```

启动成功后，浏览器打开 http://localhost:9096/ 即可看到网站。

## 停止服务

---

停止服务需要使用以下命令

```bash
docker-compose down
```
