<div align="center">

<img width=68 height=65 src="core/datacap-ui/public/static/images/logo.png" />

# DataCap

---

![Visitors](https://api.visitorbadge.io/api/visitors?path=https%3A%2F%2Fgithub.com%2FEdurtIO%2Fdatacap.git&countColor=%23263759&style=flat&labelStyle=none)
[![](https://tokei.rs/b1/github/EdurtIO/datacap)](https://github.com/EdurtIO/datacap)
![version](https://img.shields.io/github/v/release/EdurtIO/datacap.svg)

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
![GitHub branch checks state](https://img.shields.io/github/checks-status/EdurtIO/datacap/dev?style=flat-square)
![GitHub Release Date](https://img.shields.io/github/release-date/EdurtIO/datacap?style=flat-square)

![Docker Automated build](https://img.shields.io/docker/automated/qianmoq/datacap)
![Docker Image Size (latest by date)](https://img.shields.io/docker/image-size/qianmoq/datacap?style=flat-square)
![Docker Pulls](https://img.shields.io/docker/pulls/qianmoq/datacap?style=flat-square)

![GitHub commit activity](https://img.shields.io/github/commit-activity/y/EdurtIO/datacap?style=flat-square)
![GitHub contributors](https://img.shields.io/github/contributors-anon/EdurtIO/datacap?style=flat-square)
![GitHub last commit](https://img.shields.io/github/last-commit/EdurtIO/datacap?style=flat-square)

</div>

### What is datacap?

---

DataCap is integrated software for data transformation, integration, and visualization. Support a variety of data sources, file types, big data related database, relational database, NoSQL database, etc. Through the software can realize the management of multiple data sources, the data under the source of various operations conversion, making data charts, monitoring data sources and other functions.

**Example**: [try.datacap.devlive.org](http://try.datacap.devlive.org/)

| Username  | Password    |
|:---------:|:------------|
| `datacap` | `123456789` |
|  `admin`  | `12345678`  |

### Require

---

> Must-read for users: Be sure to execute the following command after cloning the code locally

```bash
cp configure/git-hook/* .git/hooks
chmod 700 .git/hooks/*
```

## Supported Connectors

---

DataCap can query data from any SQL-speaking datastore or data engine (ClickHouse, MySQL, Presto and more).

Here are some of the major database solutions that are supported:

|                  <a href="https://clickhouse.com" target="_blank"><img src="docs/docs/assets/plugin/clickhouse.png" alt="ClickHouse" height="50" /></a>                  |                             <a href="https://redis.io/" target="_blank"><img src="docs/docs/assets/plugin/redis.png" alt="Redis" height="50"/></a>                              |              <a href="https://www.postgresql.org/" target="_blank"><img src="docs/docs/assets/plugin/postgresql.png" alt="PostgreSQL" height="50"/></a>              |                              <a href="https://prestodb.io/" target="_blank"><img src="docs/docs/assets/plugin/presto.png" alt="Presto" height="50"/></a>                               |                       <a href="https://www.mysql.com" target="_blank"><img src="docs/docs/assets/plugin/mysql.png" alt="MySQL" height="50"/></a>                        |
|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
|                       <a href="https://hive.apache.org/" target="_blank"><img src="docs/docs/assets/plugin/hive.png" alt="Hive" height="50" /></a>                       |                        <a href="https://kyuubi.apache.org/" target="_blank"><img src="docs/docs/assets/plugin/kyuubi.png" alt="Kyuubi" height="50"/></a>                        |                   <a href="https://druid.apache.org/" target="_blank"><img src="docs/docs/assets/plugin/druid.png" alt="Druid" height="50" /></a>                    |                     <a href="https://www.elastic.co/" target="_blank"><img src="docs/docs/assets/plugin/elasticsearch.png" alt="ElasticSearch" height="50" /></a>                      |                         <a href="https://trino.io/" target="_blank"><img src="docs/docs/assets/plugin/trino.png" alt="Trino" height="50"/></a>                          |
|                      <a href="https://kylin.apache.org" target="_blank"><img src="docs/docs/assets/plugin/kylin.png" alt="Kylin" height="50" /></a>                      |                       <a href="https://ignite.apache.org/" target="_blank"><img src="docs/docs/assets/plugin/ignite.png" alt="Ignite" height="50" /></a>                        |                  <a href="https://www.ibm.com/db2/" target="_blank"><img src="docs/docs/assets/plugin/ibmdb2.png" alt="IBM DB2" height="50" /></a>                   |                           <a href="https://www.mongodb.com/" target="_blank"><img src="docs/docs/assets/plugin/mongodb.png" alt="MongoDB" height="50" /></a>                           |                     <a href="https://www.dremio.com/" target="_blank"><img src="docs/docs/assets/plugin/dremio.png" alt="Dremio" height="50" /></a>                     |
|                    <a href="https://www.monetdb.org/" target="_blank"><img src="docs/docs/assets/plugin/monetdb.png" alt="MonetDB" height="50" /></a>                    |                      <a href="https://phoenix.apache.org/" target="_blank"><img src="docs/docs/assets/plugin/phoenix.png" alt="Phoenix" height="50" /></a>                      |              <a href="https://www.h2database.com/html/main.html" target="_blank"><img src="docs/docs/assets/plugin/h2.png" alt="H2" height="50" /></a>               |                   <a href="https://www.microsoft.com/sql-server" target="_blank"><img src="docs/docs/assets/plugin/sqlserver.svg" alt="SqlServer" height="60" /></a>                   |                     <a href="https://www.oracle.com/" target="_blank"><img src="docs/docs/assets/plugin/oracle.png" alt="Oracle" height="50" /></a>                     |
|                       <a href="https://crate.io/" target="_blank"><img src="docs/docs/assets/plugin/cratedb.png" alt="CrateDB" height="50" /></a>                        |                     <a href="https://www.dameng.com/DM8.html" target="_blank"><img src="docs/docs/assets/plugin/dameng.png" alt="DaMeng" height="50" /></a>                     |                  <a href="https://tdengine.com/" target="_blank"><img src="docs/docs/assets/plugin/tdengine.png" alt="TDengine" height="50" /></a>                   |                           <a href="https://impala.apache.org/" target="_blank"><img src="docs/docs/assets/plugin/impala.png" alt="Impala" height="50" /></a>                           |                <a href="https://www.oceanbase.com/" target="_blank"><img src="docs/docs/assets/plugin/oceanbase.png" alt="OceanBase" height="50" /></a>                 |
|                         <a href="https://neo4j.com/" target="_blank"><img src="docs/docs/assets/plugin/neo4j.png" alt="Neo4j" height="50" /></a>                         |                         <a href="https://iotdb.apache.org/" target="_blank"><img src="docs/docs/assets/plugin/iotdb.png" alt="IoTDB" height="50" /></a>                         |               <a href="https://www.snowflake.com/" target="_blank"><img src="docs/docs/assets/plugin/snowflake.png" alt="Snowflake" height="70" /></a>               |                                  <a href="https://ydb.tech/" target="_blank"><img src="docs/docs/assets/plugin/ydb.png" alt="YDB" height="50" /></a>                                   |               <a href="https://zookeeper.apache.org/" target="_blank"><img src="docs/docs/assets/plugin/zookeeper.png" alt="Zookeeper" height="50" /></a>               |
|                       <a href="https://duckdb.org/" target="_blank"><img src="docs/docs/assets/plugin/duckdb.png" alt="DuckDB" height="50" /></a>                        |   <a href="https://www.alibabacloud.com/zh/product/object-storage-service" target="_blank"><img src="docs/docs/assets/plugin/alioss.png" alt="Aliyun OSS" height="50" /></a>    |                <a href="https://kafka.apache.org" target="_blank"><img src="docs/docs/assets/plugin/kafka.png" alt="Apache Kafka" height="50" /></a>                 |                           <a href="https://docs.ceresdb.io/" target="_blank"><img src="docs/docs/assets/plugin/ceresdb.png" alt="CeresDB" height="50" /></a>                           | <a href="https://docs.greptime.com/" target="_blank" class="connector-logo-index"><img src="docs/docs/assets/plugin/greptimedb.png" alt="GreptimeDB" height="70" /></a> |
|        <a href="https://questdb.io/" target="_blank" class="connector-logo-index"><img src="docs/docs/assets/plugin/questdb.png" alt="QuestDB" height="50" /></a>        |       <a href="https://doris.apache.org/" target="_blank" class="connector-logo-index"><img src="docs/docs/assets/plugin/doris.png" alt="Apache Doris" height="50" /></a>       | <a href="https://www.starrocks.io/" target="_blank" class="connector-logo-index"><img src="docs/docs/assets/plugin/starrocks.png" alt="StarRocks" height="50" /></a> | <a href="https://www.alibabacloud.com/product/hologres" target="_blank" class="connector-logo-index"><img src="docs/docs/assets/plugin/hologres.png" alt="Hologres" height="60" /></a> |    <a href="https://hadoop.apache.org/" target="_blank" class="connector-logo-index"><img src="docs/docs/assets/plugin/hdfs.png" alt="Apache Hdfs" height="60"/></a>    |
| <a href="https://docs.pinot.apache.org/" target="_blank" class="connector-logo-index"><img src="docs/docs/assets/plugin/pinot.png" alt="Apache Pinot" height="60" /></a> | <a href="https://cassandra.apache.org/" target="_blank" class="connector-logo-index"><img src="docs/docs/assets/plugin/cassandra.png" alt="Apache Cassandra" height="60" /></a> | <a href="https://matrixorigin.cn/" target="_blank" class="connector-logo-index"><img src="docs/docs/assets/plugin/matrixone.png" alt="MatrixOne" height="60" /></a>  |           <a href="https://www.scylladb.com/" target="_blank" class="connector-logo-index"><img src="docs/docs/assets/plugin/scylladb.png" alt="ScyllaDB" height="60" /></a>           |   <a href="https://www.paradedb.com/" target="_blank" class="connector-logo-index"><img src="docs/docs/assets/plugin/paradedb.png" alt="ParadeDB" height="80" /></a>    |
|  <a href="https://www.timescale.com/" target="_blank" class="connector-logo-index"><img src="docs/docs/assets/plugin/timescale.png" alt="Timescale" height="60" /></a>   |        <a href="https://solr.apache.org/" target="_blank" class="connector-logo-index"><img src="docs/docs/assets/plugin/solr.png" alt="Apache Solr" height="60" /></a>         | <a href="https://www.influxdata.com/" target="_blank" class="connector-logo-index"><img src="docs/docs/assets/plugin/influxdb.png" alt="InfluxDB" height="60" /></a> |                                                                                                                                                                                        |                                                                                                                                                                         |

## System architecture

---

<img src="docs/docs/assets/architecture.jpg" width="100%" />

## Stargazers over time

---

[![Star History Chart](https://api.star-history.com/svg?repos=devlive-community/datacap&type=Timeline)](https://star-history.com/#devlive-community/datacap&Timeline)

## Join Us

---

Open the DingTalk (left) or WeChat(right) software and scan the following QR code to join

<div>
  <img src="docs/docs/assets/dingtalk.png" width="200px" height="200px"/>
  <img src="docs/docs/assets/wechat.png" width="200px" height="200px"/>
</div>

## Thank you

---

[![Jetbrains](https://img.shields.io/badge/Development-Jetbrains-brightgreen?style=flat-square)](https://www.jetbrains.com/)
[![App Store](https://img.shields.io/badge/App%20Store-Rainbond-brightgreen?style=flat-square)](https://www.rainbond.com/)
[![View UI Plus](https://img.shields.io/badge/UI-View%20UI%20Plus-brightgreen?style=flat-square)](https://www.iviewui.com/view-ui-plus)

## Installation and Configuration

---

[Extended documentation for DataCap](https://datacap.devlive.org)

## Contributors

---

<a href="https://github.com/EdurtIO/datacap/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=EdurtIO/datacap" />
</a>
