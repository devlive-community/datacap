#### What is YDB ?

YDB is a fault-tolerant distributed SQL DBMS. YDB provides high availability, horizontal scalability, strict consistency, and ACID transaction support. Queries are made using an SQL dialect (YQL).

#### Environment

!!! note

    If you need to use this data source, you need to upgrade the DataCap service to >= `1.4.x`

Support Time: `2023-01-30`

#### Configure

---

DataCap uses configuration files by default [ydb.json](https://github.com/EdurtIO/incubator-datacap/blob/develop/server/src/main/etc/conf/plugins/jdbc/ydb.json)

!!! note

    If your YDB service version requires other special configurations, please refer to modifying the configuration file and restarting the DataCap service.

=== "Configure"

    | Field  |             Required              | Default Value |
    |:------:|:---------------------------------:|:-------------:|
    | `Name` | :material-check-circle: { .red }  |       -       |
    | `Host` | :material-check-circle:  { .red } |  `127.0.0.1`  |
    | `Port` | :material-check-circle:  { .red } |     `2136`    |

=== "Authorization"

    |   Field    |        Required         | Default Value |
    |:----------:|:-----------------------:|:-------------:|
    | `Username` | :material-close-circle: |       -       |
    | `Password` | :material-close-circle: |       -       |

=== "Advanced"

    |   Field    |             Required              | Default Value |
    |:----------:|:---------------------------------:|:-------------:|
    | `Database` | :material-check-circle:  { .red } |    `local`    |

#### Version (Validation)

---

!!! warning

    The online service has not been tested yet, if you have detailed test results, please submit [issues](https://github.com/EdurtIO/incubator-datacap/issues/new/choose) to us

- [x] 2.1.x
