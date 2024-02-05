---
title: ParadeDB
---

<img src="/assets/plugin/paradedb.png" class="connector-logo" style="width: 200px; margin-top: 35px;" />

#### Introduce

ParadeDB is an Elasticsearch alternative built on Postgres. We’re modernizing the features of Elasticsearch, starting with real-time search and analytics.

ParadeDB is not a fork of Postgres, but regular Postgres with custom extensions installed. ParadeDB itself ships with Postgres 16.

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
