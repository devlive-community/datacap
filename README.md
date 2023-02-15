<div align="center">

<img width=68 height=65 src="./core/web/console-fe/public/static/images/logo.png" />

# DataCap

---

![](https://visitor-badge.glitch.me/badge?page_id=datacap)
![Visitors](https://api.visitorbadge.io/api/combined?path=https%3A%2F%2Fgithub.com%2FEdurtIO%2Fdatacap.git&countColor=%23263759&style=flat&labelStyle=none)
[![](https://tokei.rs/b1/github/EdurtIO/datacap)](https://github.com/EdurtIO/datacap)
![version](https://img.shields.io/github/v/release/EdurtIO/datacap.svg)

![GitHub branch checks state](https://img.shields.io/github/checks-status/EdurtIO/datacap/develop?style=flat-square)
![GitHub Release Date](https://img.shields.io/github/release-date/EdurtIO/datacap?style=flat-square)

![GitHub commit activity](https://img.shields.io/github/commit-activity/y/EdurtIO/datacap?style=flat-square)
![GitHub contributors](https://img.shields.io/github/contributors-anon/EdurtIO/datacap?style=flat-square)
![GitHub last commit](https://img.shields.io/github/last-commit/EdurtIO/datacap?style=flat-square)

</div>

### What is datacap?

---

DataCap is integrated software for data transformation, integration, and visualization. Support a variety of data sources, file types, big data related database, relational database, NoSQL database, etc. Through the software can realize the management of multiple data sources, the data under the source of various operations conversion, making data charts, monitoring data sources and other functions.

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

<p align="center">
    <a href="https://clickhouse.com" target="_blank">
        <img src="assets/plugin/clickhouse.png" alt="ClickHouse" height="50" />
    </a>&nbsp;
    <a href="https://www.mysql.com" target="_blank">
        <img src="assets/plugin/mysql.png" alt="MySQL" height="50"/>
    </a>&nbsp;
    <a href="https://prestodb.io/" target="_blank">
        <img src="assets/plugin/presto.png" alt="Presto" height="50"/>
    </a>&nbsp;
    <a href="https://redis.io/" target="_blank">
        <img src="assets/plugin/redis.png" alt="Redis" height="50"/>
    </a>&nbsp;
    <a href="https://www.postgresql.org/" target="_blank">
        <img src="assets/plugin/postgresql.png" alt="PostgreSQL" height="50"/>
    </a>&nbsp;
    <a href="https://trino.io/" target="_blank">
        <img src="assets/plugin/trino.png" alt="Trino" height="50"/>
    </a>&nbsp;
    <a href="https://www.elastic.co/" target="_blank">
        <img src="assets/plugin/elasticsearch.png" alt="ElasticSearch" height="50" />
    </a>&nbsp;
    <a href="https://druid.apache.org/" target="_blank">
        <img src="assets/plugin/druid.png" alt="Druid" height="50" />
    </a>&nbsp;
    <a href="https://kyuubi.apache.org/" target="_blank">
        <img src="assets/plugin/kyuubi.png" alt="Kyuubi" height="50"/>
    </a>&nbsp;
    <a href="https://hive.apache.org/" target="_blank">
        <img src="assets/plugin/hive.png" alt="Hive" height="50" />
    </a>&nbsp;
    <a href="https://kylin.apache.org" target="_blank">
        <img src="assets/plugin/kylin.png" alt="Kylin" height="50" />
    </a>&nbsp;
    <a href="https://ignite.apache.org/" target="_blank">
        <img src="assets/plugin/ignite.png" alt="Ignite" height="50" />
    </a>&nbsp;
    <a href="https://www.ibm.com/db2/" target="_blank">
        <img src="assets/plugin/ibmdb2.png" alt="IBM DB2" height="50" />
    </a>&nbsp;
    <a href="https://www.mongodb.com/" target="_blank">
        <img src="assets/plugin/mongodb.png" alt="MongoDB" height="50" />
    </a>&nbsp;
    <a href="https://www.dremio.com/" target="_blank">
        <img src="assets/plugin/dremio.png" alt="Dremio" height="50" />
    </a>&nbsp;
    <a href="https://www.monetdb.org/" target="_blank">
        <img src="assets/plugin/monetdb.png" alt="MonetDB" height="50" />
    </a>&nbsp;
    <a href="https://phoenix.apache.org/" target="_blank">
        <img src="assets/plugin/phoenix.png" alt="Phoenix" height="50" />
    </a>&nbsp;
    <a href="https://www.h2database.com/html/main.html" target="_blank">
        <img src="assets/plugin/h2.png" alt="H2" height="50" />
    </a>&nbsp;
    <a href="https://www.microsoft.com/sql-server" target="_blank">
        <img src="assets/plugin/sqlserver.svg" alt="SqlServer" height="60" />
    </a>&nbsp;
    <a href="https://www.oracle.com/" target="_blank">
        <img src="assets/plugin/oracle.png" alt="Oracle" height="50" />
    </a>&nbsp;
    <a href="https://crate.io/" target="_blank">
        <img src="assets/plugin/cratedb.png" alt="CrateDB" height="50" />
    </a>&nbsp;
    <a href="https://www.dameng.com/DM8.html" target="_blank">
        <img src="assets/plugin/dameng.png" alt="DaMeng" height="50" />
    </a>&nbsp;
    <a href="https://tdengine.com/" target="_blank">
        <img src="assets/plugin/tdengine.png" alt="TDengine" height="50" />
    </a>&nbsp;
    <a href="https://impala.apache.org/" target="_blank">
        <img src="assets/plugin/impala.png" alt="Impala" height="50" />
    </a>&nbsp;
    <a href="https://www.oceanbase.com/" target="_blank">
        <img src="assets/plugin/oceanbase.png" alt="OceanBase" height="50" />
    </a>&nbsp;
    <a href="https://neo4j.com/" target="_blank">
        <img src="assets/plugin/neo4j.png" alt="Neo4j" height="50" />
    </a>&nbsp;
    <a href="https://iotdb.apache.org/" target="_blank">
        <img src="assets/plugin/iotdb.png" alt="IoTDB" height="50" />
    </a>&nbsp;
    <a href="https://www.snowflake.com/" target="_blank">
        <img src="assets/plugin/snowflake.png" alt="Snowflake" height="70" />
    </a>&nbsp;
    <a href="https://ydb.tech/" target="_blank">
        <img src="assets/plugin/ydb.png" alt="YDB" height="50" />
    </a>&nbsp;
    <a href="https://zookeeper.apache.org/" target="_blank">
        <img src="assets/plugin/zookeeper.png" alt="Zookeeper" height="50" />
    </a>
</p>

## Stargazers over time

---

[![Stargazers over time](https://starchart.cc/EdurtIO/datacap.svg)](https://starchart.cc/EdurtIO/datacap)

## Join Us

---

Open the DingTalk software and scan the following QR code to join

<img src="assets/dingtalk.png" width="200px" height="250px"></img>

## Thank you

---

- [Jetbrains](https://www.jetbrains.com/)

## Installation and Configuration

---

[Extended documentation for DataCap](https://datacap.incubator.edurt.io)

## Contributors

---

<a href="https://github.com/EdurtIO/datacap/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=EdurtIO/datacap" />
</a>
