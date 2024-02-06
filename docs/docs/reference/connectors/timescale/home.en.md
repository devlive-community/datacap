---
title: Timescale
---

<img src="/assets/plugin/timescale.png" class="connector-logo" style="width: 200px; margin-top: 35px;" />

#### Introduce

Timescale is a PostgreSQL data platform for time-series, events, and analytics. It gives you the reliability of PostgreSQL, the time-series superpowers of TimescaleDB, and the peace of mind of a fully managed service. It provides features like automatic backup and restore, high availability with replication, seamless scaling and resizing, and much more.

#### Environment

---

!!! note

    If you need to use this data source, you need to upgrade the DataCap service to >= `2024.02.1`

Support Time: `2024-02-05`

#### Configure

---

=== "Configure"

    | Field  |             Required              | Default Value |
    |:------:|:---------------------------------:|:-------------:|
    | `Name` | :material-check-circle: { .red }  |       -       |
    | `Host` | :material-check-circle:  { .red } |  `127.0.0.1`  |
    | `Port` | :material-check-circle:  { .red } |     `5432`    |

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

    The online service has not been tested yet, if you have detailed test results, please submit [issues](https://github.com/devlive-community/datacap/issues/new/choose) to us

- [x] `all`
