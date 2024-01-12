---
title: MatrixOne
---

<img src="/assets/plugin/matrixone.png" class="connector-logo" style="width: 200px; margin-top: 35px;" />

#### Introduce

MatrixOne is a future-oriented hyper-converged cloud and edge native DBMS that supports transactional, analytical, and streaming workloads with a simplified and distributed database engine, across multiple data centers, clouds, edges and other heterogeneous infrastructures.

#### Environment

---

!!! note

    If you need to use this data source, you need to upgrade the DataCap service to >= `2024.01.1`

Support Time: `2024-01-12`

#### Configure

---


!!! note

    If your MatrixOne service version requires other special configurations, please refer to modifying the configuration file and restarting the DataCap service.

=== "Configure"

    | Field  |             Required              | Default Value |
    |:------:|:---------------------------------:|:-------------:|
    | `Name` | :material-check-circle: { .red }  |       -       |
    | `Host` | :material-check-circle:  { .red } |  `127.0.0.1`  |
    | `Port` | :material-check-circle:  { .red } |     `6001`    |

=== "Authorization"

    |   Field    |             Required              | Default Value |
    |:----------:|:---------------------------------:|:-------------:|
    | `Username` | :material-check-circle: { .red }  |       -       |
    | `Password` | :material-check-circle:  { .red } |       -       |
    |   `SSL`    |      :material-close-circle:      |    `false`    |

=== "Advanced"

    |   Field    |             Required             | Default Value |
    |:----------:|:--------------------------------:|:-------------:|
    | `Database` | :material-check-circle: { .red } |   ` `   |

=== "Custom"

    There is no default configuration, and you can add it by user

#### 功能

---

|      Function       |    Supported     |
|:-------------------:|:----------------:|
|       AD Hoc        | :material-check: |
|       DataSet       | :material-check: |
|      Pipeline       | :material-check: |
| Metadata management | :material-close: |

#### Version

---

!!! warning

    The online service has not been tested yet, if you have detailed test results, please submit [issues](https://github.com/EdurtIO/datacap/issues/new/choose) to us

- [x] `1.1.0`
