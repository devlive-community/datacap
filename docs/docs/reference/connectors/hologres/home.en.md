---
title: Hologres
status: new
---

<img src="/assets/plugin/hologres.png" class="connector-logo" />

#### What is Hologres ?

Hologres is an all-in-one real-time data warehouse engine that is compatible with PostgreSQL. It supports online analytical processing (OLAP) and ad hoc analysis of PB-scale data. Hologres supports online data serving at high concurrency and low latency. It is deeply integrated with MaxCompute, Flink and DataWorks, provides a full-stack data warehouse solution that integrates online and offline processing.

#### Environment

!!! note

    If you need to use this data source, you need to upgrade the DataCap service to >= `1.9.x`

Support Time: `2023-04-25`

#### Configure

---

!!! note

    If your Hologres service version requires other special configurations, please refer to modifying the configuration file and restarting the DataCap service.

=== "Configure"

    | Field  |             Required              | Default Value |
    |:------:|:---------------------------------:|:-------------:|
    | `Name` | :material-check-circle: { .red }  |       -       |
    | `Host` | :material-check-circle:  { .red } |  `hologres-cn-regison.aliyuncs.com`  |
    | `Port` | :material-check-circle:  { .red } |     `80`    |

=== "Authorization"

    |   Field    |        Required         | Default Value |
    |:----------:|:-----------------------:|:-------------:|
    | `Username` | :material-close-circle: |       -       |
    | `Password` | :material-close-circle: |       -       |

=== "Advanced"

    |   Field    |        Required         | Default Value |
    |:----------:|:-----------------------:|:-------------:|
    | `Database` | :material-close-circle: |       -       |

#### Version (Validation)

---

!!! warning

    The online service has not been tested yet, if you have detailed test results, please submit [issues](https://github.com/EdurtIO/datacap/issues/new/choose) to us

- [x] all
