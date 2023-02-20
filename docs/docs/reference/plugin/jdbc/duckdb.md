#### What is DuckDB ?

DuckDB is an in-process SQL OLAP database management system.

#### Environment

!!! note

    If you need to use this data source, you need to upgrade the DataCap service to >= `1.6.x`

Support Time: `2023-02-20`

#### Configure

---

DataCap uses configuration files by default [duckdb.json](https://github.com/EdurtIO/datacap/blob/develop/server/src/main/etc/conf/plugins/jdbc/duckdb.json)

!!! note

    If your DuckDB service version requires other special configurations, please refer to modifying the configuration file and restarting the DataCap service.

=== "Configure"

    | Field  |             Required              | Default Value |
    |:------:|:---------------------------------:|:-------------:|
    | `Name` | :material-check-circle: { .red }  |       -       |
    | `Host` | :material-check-circle:  { .red } |  `/root`  |
    | `Port` | :material-check-circle:  { .red } |     `0`    |

=== "Advanced"

    |   Field    |             Required              | Default Value |
    |:----------:|:---------------------------------:|:-------------:|
    | `Database` | :material-check-circle:  { .red } |    `local`    |

#### Version (Validation)

---

!!! warning

    The online service has not been tested yet, if you have detailed test results, please submit [issues](https://github.com/EdurtIO/datacap/issues/new/choose) to us

- [x] 0.7.0
