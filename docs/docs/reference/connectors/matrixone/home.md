---
title: MatrixOne
---

<img src="/assets/plugin/matrixone.png" class="connector-logo" style="width: 200px; margin-top: 35px;" />

#### 介绍

MatrixOne 是一款面向未来的超融合云和边缘原生 DBMS，通过简化的分布式数据库引擎，跨多个数据中心、云、边缘和其他异构基础设施支持事务、分析和流式工作负载。

#### 环境

---

!!! note

    如果需要使用此数据源，则需要将 DataCap 服务升级到 >= `2024.01.1`

支持时间: `2024-01-12`

#### 配置

---

=== "配置"

    | 属性  |             是否必要              | 默认值 |
    |:------:|:---------------------------------:|:-------------:|
    | `Name` | :material-check-circle: { .red }  |       -       |
    | `Host` | :material-check-circle:  { .red } |  `127.0.0.1`  |
    | `Port` | :material-check-circle:  { .red } |     `6001`    |

=== "授权"

    | 属性  |             是否必要              | 默认值 |
    |:----------:|:---------------------------------:|:-------------:|
    | `Username` | :material-check-circle: { .red }  |       -       |
    | `Password` | :material-check-circle:  { .red } |       -       |
    |   `SSL`    |      :material-close-circle:      |    `false`    |

=== "高级"

    | 属性  |             是否必要              | 默认值 |
    |:----------:|:--------------------------------:|:-------------:|
    | `Database` | :material-check-circle: { .red } |   ` `   |

=== "自定义"

    暂无默认配置，支持用户自定义添加

#### 功能

---

|  功能   |        支持        |
|:-----:|:----------------:|
| 即席查询  | :material-check: |
|  数据集  | :material-check: |
|  流水线  | :material-check: |
| 元数据管理 | :material-close: |

#### 版本

---

!!! warning

    在线服务尚未测试，如果您有详细的测试结果，请提交 [issues](https://github.com/EdurtIO/datacap/issues/new/choose)

- [x] `1.1.0`
