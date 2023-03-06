---
title: Apache Kafka
status: new
---

#### What is Apache Kafka ?

Apache Kafka is an open-source distributed event streaming platform used by thousands of companies for high-performance data pipelines, streaming analytics, data integration, and mission-critical applications.

#### Environment

---

!!! note

    If you need to use this data source, you need to upgrade the DataCap service to >= `1.7.x`

Support Time: `2023-03-06`

#### SQL statement syntax

---

This chapter describes the SQL syntax used in DataCap Kafka plugin.

[SHOW TOPICS](../../../sql_syntax/connectors/native/kafka/show_topics.md) <br />
[SHOW CONSUMERS](../../../sql_syntax/connectors/native/kafka/show_consumers.md)

#### Configure

---

DataCap uses configuration files by default [kafka.json](https://github.com/EdurtIO/datacap/blob/dev/server/src/main/etc/conf/plugins/native/kafka.json)

!!! note

    If your Apache Kafka service version requires other special configurations, please refer to modifying the configuration file and restarting the DataCap service.

=== "Configure"

    | Field  |             Required              |  Default Value   |
    |:------:|:---------------------------------:|:----------------:|
    | `Name` | :material-check-circle: { .red }  |        -         |
    | `Host` | :material-check-circle:  { .red } | `localhost:9092` |

#### Version (Validation)

---

!!! warning

    The online service has not been tested yet, if you have detailed test results, please submit [issues](https://github.com/EdurtIO/datacap/issues/new/choose) to us

- [x] `1.0.0`
- [x] `1.1.0`
- [x] `1.2.0`
