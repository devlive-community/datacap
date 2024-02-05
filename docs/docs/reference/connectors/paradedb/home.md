---
title: ParadeDB
---

<img src="/assets/plugin/paradedb.png" class="connector-logo" style="width: 200px; margin-top: 35px;" />

#### 介绍

ParadeDB 是基于 Postgres 构建的 Elasticsearch 替代方案。我们正在对 Elasticsearch 的功能进行现代化改造， 从实时搜索和分析开始。

ParadeDB 不是 Postgres 的分支，而是安装了自定义扩展的常规 Postgres。ParadeDB 本身 随 Postgres 16 一起提供。

#### 环境

---

!!! note

    如果需要使用此数据源，则需要将 DataCap 服务升级到 >= `2024.02.1`

支持时间: `2024-02-05`

#### 配置

---

=== "配置"

    | 属性  |             是否必要              | 默认值 |
    |:------:|:---------------------------------:|:-------------:|
    | `Name` | :material-check-circle: { .red }  |       -       |
    | `Host` | :material-check-circle:  { .red } |  `127.0.0.1`  |
    | `Port` | :material-check-circle:  { .red } |     `5432`    |

=== "高级"

    |   属性    |             是否必要             | 默认值 |
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

    在线服务尚未测试，如果您有详细的测试结果，请提交 [issues](https://github.com/devlive-community/datacap/issues/new/choose)

- [x] `all`
