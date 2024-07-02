---
template: main.html
title: DataCap

hide:
- navigation
- toc
- footer
---

<style xmlns="http://www.w3.org/1999/html">
.md-typeset h1 {
  text-align: center;
  color: transparent;
}
.md-typeset h2 {
  text-align: center;
  font-weight: 1000;
  font-size: 50px;
  margin-top: 60px;
  margin-bottom: 0;
}
</style>

<div style="text-align: center;">
    <img width="100" height="85" style="margin-top: -50px;" src="/assets/logo.png" />
<p/>DataCap is integrated software for data transformation, integration, and visualization.
    <p/>Supports multiple data sources, file types, big data-related databases, relational databases, NoSQL databases, and more.
    <p/>Through the software, the management of multiple data sources can be realized, and various operations can be performed on the data under the source, data charts can be made, data sources can be monitored, and other functions can be performed.
    <p/>
    <a target="_blank" href="https://gitee.com/devlive-community/datacap/members">
        <img src='https://gitee.com/devlive-community/datacap/badge/fork.svg?theme=white' alt='fork'/>
    </a>
    <a target="_blank" href="https://gitee.com/devlive-community/datacap/stargazers">
        <img src='https://gitee.com/devlive-community/datacap/badge/star.svg?theme=white' alt='star'/>
    </a>
    <a target="_blank" href="https://github.com/devlive-community/datacap/fork">
        <img alt="GitHub stars" src="https://img.shields.io/github/forks/devlive-community/datacap?logo=github">
    </a>
    <a target="_blank" href="https://github.com/devlive-community/datacap/stargazers">
        <img alt="GitHub stars" src="https://img.shields.io/github/stars/devlive-community/datacap?logo=github">
    </a>
    <p/> 
    <p/>
    <a href="/reference/getStarted/install.html" title="Get Started" class="md-button">
        Get Started
    </a>
    <a href="/download.html" title="Download" class="md-button">
      Download
    </a>
    <a href="https://github.com/devlive-community/datacap" target="_blank" title="Join us on GitHub" class="md-button md-button--primary">
      Join us on GitHub
    </a>
    <a href="http://try.datacap.devlive.org/" target="_blank" title="Check out the online demo" class="md-button md-button--primary">
      Check out the online demo
    </a>
    <p/><p/><p/><p/>
</div>

## Overview

<div style="max-width: 60%; text-align: center;margin: 0 auto;">
<p style="text-align: center;">
Datacap is a fast, lightweight, and intuitive system. It can be used to manage multiple data sources, perform various operations on the data under the source, make data charts, monitor data sources, and other functions. It can also be used as a database management system to add, delete, modify and query the data under the source.
</p>
</div>

<div style="max-width: 800px; margin: 0 auto" class="grid cards" markdown>

- __Powerful but easy to use__ 

    ---

    DataCap uses a simple SQL IDE to quickly and easily integrate and explore your data.

- __Integrate with modern databases__

    ---

    DataCap can connect to any SQL-based | via various protocols such as JDBC, Native, Http, etc NoSQL data sources.

- __Highly customizable__

    ---

    DataCap can quickly customize your data sources by implementing the plug-in SPI provided. It is also possible to customize your data conversion integration tool by implementing an actuator SPI.

- __Join (DingTalk | WeChat)__

    ---

    <img src="/assets/dingtalk.png" alt="DingTalk" style="height: 160px;" /><img src="/assets/wechat.png" alt="Wechat" style="height: 160px;" />

</div>

## Supported connectors

<div class="grid cards" markdown>

- <a href="https://clickhouse.com" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/clickhouse.png" alt="ClickHouse" />
  </a>
- <a href="https://www.mysql.com" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/mysql.png" alt="MySQL" />
  </a>
- <a href="https://prestodb.io/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/presto.png" alt="Presto" height="50"/>
  </a>
- <a href="https://redis.io/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/redis.png" alt="Redis" height="50"/>
  </a>
- <a href="https://www.postgresql.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/postgresql.png" alt="PostgreSQL" height="50"/>
  </a>
- <a href="https://trino.io/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/trino.png" alt="Trino" height="50"/>
  </a>
- <a href="https://www.elastic.co/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/elasticsearch.png" alt="ElasticSearch" height="50" />
  </a>
- <a href="https://druid.apache.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/druid.png" alt="Druid" height="50" />
  </a>
- <a href="https://kyuubi.apache.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/kyuubi.png" alt="Kyuubi" height="50"/>
  </a>
- <a href="https://hive.apache.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/hive.png" alt="Hive" height="50" />
  </a>
- <a href="https://kylin.apache.org" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/kylin.png" alt="Kylin" height="50" />
  </a>
- <a href="https://ignite.apache.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/ignite.png" alt="Ignite" height="50" />
  </a>
- <a href="https://www.ibm.com/db2/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/ibmdb2.png" alt="IBM DB2" height="50" />
  </a>
- <a href="https://www.mongodb.com/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/mongodb.png" alt="MongoDB" height="50" />
  </a>
- <a href="https://www.dremio.com/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/dremio.png" alt="Dremio" height="50" />
  </a>
- <a href="https://www.monetdb.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/monetdb.png" alt="MonetDB" height="50" />
  </a>
- <a href="https://phoenix.apache.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/phoenix.png" alt="Phoenix" height="50" />
  </a>
- <a href="https://www.h2database.com/html/main.html" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/h2.png" alt="H2" height="50" />
  </a>
- <a href="https://www.microsoft.com/sql-server" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/sqlserver.svg" alt="SqlServer" height="60" />
  </a>
- <a href="https://www.oracle.com/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/oracle.png" alt="Oracle" height="50" />
  </a>
- <a href="https://crate.io/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/cratedb.png" alt="CrateDB" height="50" />
  </a>
- <a href="https://www.dameng.com/DM8.html" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/dameng.png" alt="DaMeng" height="50" />
  </a>
- <a href="https://tdengine.com/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/tdengine.png" alt="TDengine" height="50" />
  </a>
- <a href="https://impala.apache.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/impala.png" alt="Impala" height="50" />
  </a>
- <a href="https://www.oceanbase.com/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/oceanbase.png" alt="OceanBase" height="50" />
  </a>
- <a href="https://neo4j.com/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/neo4j.png" alt="Neo4j" height="50" />
  </a>
- <a href="https://iotdb.apache.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/iotdb.png" alt="IoTDB" height="50" />
  </a>
- <a href="https://www.snowflake.com/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/snowflake.png" alt="Snowflake" height="70" />
  </a>
- <a href="https://ydb.tech/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/ydb.png" alt="YDB" height="50" />
  </a>
- <a href="https://zookeeper.apache.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/zookeeper.png" alt="Zookeeper" height="50" />
  </a>
- <a href="https://duckdb.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/duckdb.png" alt="DuckDB" height="50" />
  </a>
- <a href="https://www.alibabacloud.com/zh/product/object-storage-service" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/alioss.png" alt="Aliyun OSS" height="50" />
  </a>
- <a href="https://kafka.apache.org" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/kafka.png" alt="Apache Kafka" height="50" />
  </a>
- <a href="https://docs.ceresdb.io/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/ceresdb.png" alt="CeresDB" height="50" />
  </a>
- <a href="https://docs.greptime.com/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/greptimedb.png" alt="GreptimeDB" height="70" />
  </a>
- <a href="https://questdb.io/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/questdb.png" alt="QuestDB" height="50" />
  </a>
- <a href="https://doris.apache.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/doris.png" alt="Apache Doris" height="50" />
  </a>
- <a href="https://www.starrocks.io/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/starrocks.png" alt="StarRocks" height="50" />
  </a>
- <a href="https://www.alibabacloud.com/product/hologres" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/hologres.png" alt="Hologres" height=60" />
  </a>
- <a href="https://hadoop.apache.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/hdfs.png" alt="Apache Hdfs" height=60" />
  </a>
- <a href="https://docs.pinot.apache.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/pinot.png" alt="Apache Pinot" height=60" />
  </a>
- <a href="https://cassandra.apache.org/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/cassandra.png" alt="Apache Cassandra" height=60" />
  </a>
- <a href="https://matrixorigin.cn/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/matrixone.png" alt="MatrixOne" height=60" />
  </a>
- <a href="https://www.scylladb.com/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/scylladb.png" alt="ScyllaDB" height=60" />
  </a>
- <a href="https://www.scylladb.com/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/paradedb.png" alt="ParadeDB" height=60" />
  </a>
- <a href="https://www.timescale.com/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/timescale.png" alt="Timescale" height=60" />
  </a>
- <a href="https://www.timescale.com/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/solr.png" alt="Solr" height=60" />
  </a>
- <a href="https://www.timescale.com/" target="_blank" class="connector-logo-index">
      <img src="/assets/plugin/influxdb.png" alt="InfluxDB" height=80" />
  </a>

</div>
