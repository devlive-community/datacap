---
title: ScyllaDB
---

<img src="/assets/plugin/scylladb.png" class="connector-logo" style="width: 200px; margin-top: 35px;" />

#### Introduce

ScyllaDB is a distributed NoSQL wide-column database for data-intensive apps that require high performance and low latency.

#### Environment

---

!!! note

    If you need to use this data source, you need to upgrade the DataCap service to >= `2024.01.1`

Support Time: `2024-01-12`

#### Configure

---

=== "Configure"

    | Field  |             Required              | Default Value |
    |:------:|:---------------------------------:|:-------------:|
    | `Name` | :material-check-circle: { .red }  |       -       |
    | `Host` | :material-check-circle:  { .red } |  `127.0.0.1`  |
    | `Port` | :material-check-circle:  { .red } |     `9042`    |

=== "Advanced"

    |   Field    |             Required             | Default Value |
    |:----------:|:--------------------------------:|:-------------:|
    | `Database` | :material-check-circle: { .red } |   ` `   |

#### Function

---

|      Function       |    Supported     |
|:-------------------:|:----------------:|
|       AD Hoc        | :material-check: |
|       DataSet       | :material-check: |
|      Pipeline       | :material-close: |
| Metadata management | :material-close: |

#### Version

---

!!! Version

    The online service has not been tested yet, if you have detailed test results, please submit [issues](https://github.com/EdurtIO/datacap/issues/new/choose) to us

- [x] `all`
