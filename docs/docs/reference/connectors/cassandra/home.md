---
title: Apache Cassandra
status: 1.11.0
---

<img src="/assets/plugin/cassandra.png" class="connector-logo" style="width: 100px;" />

#### What is Cassandra ?

Apache Cassandra® powers mission-critical deployments with improved performance and unparalleled levels of scale in the cloud.

#### Environment

---

!!! note

    If you need to use this data source, you need to upgrade the DataCap service to >= `1.11.x`

Support Time: `2023-06-07`

#### Configure

---


!!! note

    If your plugin service version requires other special configurations, please refer to modifying the configuration file and restarting the DataCap service.

=== "Configure"

    | Field  |             Required              | Default Value |
    |:------:|:---------------------------------:|:-------------:|
    | `Name` | :material-check-circle: { .red }  |       -       |
    | `Host` | :material-check-circle:  { .red } |  `127.0.0.1`  |
    | `Port` | :material-check-circle:  { .red } |     `9042`    |

=== "Advanced"

    |   Field    |             Required             | Default Value |
    |:----------:|:--------------------------------:|:-------------:|
    | `Database` | :material-check-circle: { .red } | `datacenter`  |

#### Version (Validation)

---

!!! warning

    The online service has not been tested yet, if you have detailed test results, please submit [issues](https://github.com/EdurtIO/datacap/issues/new/choose) to us

- [x] `0.4.x`
