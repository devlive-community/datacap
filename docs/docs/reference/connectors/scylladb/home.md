---
title: ScyllaDB
---

<img src="/assets/plugin/scylladb.png" class="connector-logo" style="width: 200px; margin-top: 35px;" />

#### 介绍

ScyllaDB 是一个分布式 NoSQL 宽列数据库，适用于需要高性能和低延迟的数据密集型应用程序。

#### 环境

---

!!! note

    如果需要使用此数据源，则需要将 DataCap 服务升级到 >= `2024.01.1`

支持时间: `2024-01-12`

#### 配置

---

=== "配置"

    | Field  |             Required              | Default Value |
    |:------:|:---------------------------------:|:-------------:|
    | `Name` | :material-check-circle: { .red }  |       -       |
    | `Host` | :material-check-circle:  { .red } |  `127.0.0.1`  |
    | `Port` | :material-check-circle:  { .red } |     `9042`    |

=== "高级"

    |   Field    |             Required             | Default Value |
    |:----------:|:--------------------------------:|:-------------:|
    | `Database` | :material-check-circle: { .red } |   ` `   |

#### 功能

---

|  功能   |        支持        |
|:-----:|:----------------:|
| 即席查询  | :material-check: |
|  数据集  | :material-check: |
|  流水线  | :material-close: |
| 元数据管理 | :material-close: |

#### 版本

---

!!! warning

    在线服务尚未测试，如果您有详细的测试结果，请提交 [issues](https://github.com/EdurtIO/datacap/issues/new/choose)

- [x] `all`
