---
title: MySQL
---

<img src="/assets/plugin/mysql.png" class="connector-logo" />

#### What is MySQL ?

MySQL is one of the most recognizable technologies in the modern big data ecosystem. Often called the most popular database and currently enjoying widespread, effective use regardless of industry, it’s clear that anyone involved with enterprise data or general IT should at least aim for a basic familiarity of MySQL.

#### Environment

!!! note

    If you need to use this data source, you need to upgrade the DataCap service to >= `1.0.x`

Support Time: `2022-09-19`

#### Configure

---

DataCap uses configuration files by default [mysql.json](https://github.com/EdurtIO/datacap/blob/develop/server/src/main/etc/conf/plugins/jdbc/mysql.json)

!!! note

    If your MySQL service version requires other special configurations, please refer to modifying the configuration file and restarting the DataCap service.

=== "Configure"

    | Field  |             Required              | Default Value |
    |:------:|:---------------------------------:|:-------------:|
    | `Name` | :material-check-circle: { .red }  |       -       |
    | `Host` | :material-check-circle:  { .red } |  `127.0.0.1`  |
    | `Port` | :material-check-circle:  { .red } |     `3306`    |

=== "Authorization"

    |   Field    |             Required              | Default Value |
    |:----------:|:---------------------------------:|:-------------:|
    | `Username` | :material-check-circle: { .red }  |       -       |
    | `Password` | :material-check-circle:  { .red } |       -       |
    |   `SSL`    |      :material-close-circle:      |    `false`    |

=== "Advanced"

    |   Field    |             Required             | Default Value |
    |:----------:|:--------------------------------:|:-------------:|
    | `Database` | :material-check-circle: { .red } |   `default`   |

=== "Custom"

    !!! note

        You can add the already supported MySQL parameters by adding Key Value, the parameters can be [reference document](https://www.mysql.com/cn/products/connector/)

    Default:
    
    |              Key              | value  | 
    |:-----------------------------:|:------:|
    | `useOldAliasMetadataBehavior` | `true` |

#### Version (Validation)

---

!!! warning

    The online service has not been tested yet, if you have detailed test results, please submit [issues](https://github.com/EdurtIO/datacap/issues/new/choose) to us

- [x] 5.x
- [x] 6.x
- [x] 7.x
