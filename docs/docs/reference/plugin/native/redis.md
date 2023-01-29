#### What is Redis ?

The open source, in-memory data store used by millions of developers as a database, cache, streaming engine, and message broker.

#### Environment

!!! note

    If you need to use this data source, you need to upgrade the DataCap service to >= `1.3.x`

Support Time: `2022-12-01`

#### Configure

---

DataCap uses configuration files by default [redis.json](https://github.com/EdurtIO/incubator-datacap/blob/develop/server/src/main/etc/conf/plugins/native/redis.json)

!!! note

    If your Redis service version requires other special configurations, please refer to modifying the configuration file and restarting the DataCap service.

=== "Configure"

    | Field  |             Required              | Default Value |
    |:------:|:---------------------------------:|:-------------:|
    | `Name` | :material-check-circle: { .red }  |       -       |
    | `Host` | :material-check-circle:  { .red } |  `127.0.0.1`  |
    | `Port` | :material-check-circle:  { .red } |    `6379`     |

=== "Authorization"

    |   Field    |        Required         | Default Value |
    |:----------:|:-----------------------:|:-------------:|
    | `Password` | :material-close-circle: |       -       |

#### Version (Validation)

---

!!! warning

    The online service has not been tested yet, if you have detailed test results, please submit [issues](https://github.com/EdurtIO/incubator-datacap/issues/new/choose) to us

- [x] 6.x
- [x] 7.x
