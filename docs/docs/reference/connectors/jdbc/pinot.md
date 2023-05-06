---
title: Apache Pinot
status: 1.10.0
---

<img src="/assets/plugin/pinot.png" class="connector-logo" style="width: 100px;" />

#### What is Pinot ?

Apache Pinot is a real-time distributed OLAP datastore purpose-built for low-latency, high-throughput analytics, and perfect for user-facing analytical workloads.

#### Environment

---

!!! note

    If you need to use this data source, you need to upgrade the DataCap service to >= `1.10.x`

Support Time: `2023-05-06`

#### Configure

---


!!! note

    If your Pinot service version requires other special configurations, please refer to modifying the configuration file and restarting the DataCap service.

=== "Configure"

    | Field  |             Required              | Default Value |
    |:------:|:---------------------------------:|:-------------:|
    | `Name` | :material-check-circle: { .red }  |       -       |
    | `Host` | :material-check-circle:  { .red } |  `127.0.0.1`  |
    | `Port` | :material-check-circle:  { .red } |     `9000`    |

=== "Authorization"

    |   Field    |             Required              | Default Value |
    |:----------:|:---------------------------------:|:-------------:|
    | `Username` | :material-check-circle: { .red }  |       -       |
    | `Password` | :material-check-circle:  { .red } |       -       |

#### Version (Validation)

---

!!! warning

    The online service has not been tested yet, if you have detailed test results, please submit [issues](https://github.com/EdurtIO/datacap/issues/new/choose) to us

- [x] `0.8.x`
