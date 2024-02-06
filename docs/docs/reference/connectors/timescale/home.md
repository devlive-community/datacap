---
title: Timescale
---

<img src="/assets/plugin/timescale.png" class="connector-logo" style="width: 200px; margin-top: 35px;" />

#### 介绍

Timescale 是一个用于时间序列、事件和分析的 PostgreSQL 数据平台。 它为您提供 PostgreSQL 的可靠性、TimescaleDB 的时间序列超能力以及完全托管服务的安心。 它提供自动备份和恢复、复制的高可用性、无缝扩展和调整大小等功能。

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
