---
title: Apache Hadoop HDFS
---

<img src="/assets/plugin/hdfs.png" class="connector-logo" style="width: 100px;" />

#### What is HDFS ?

The Hadoop Distributed File System (HDFS) is a distributed file system designed to run on commodity hardware. It has many similarities with existing distributed file systems.

#### Environment

---

!!! note

    If you need to use this data source, you need to upgrade the DataCap service to >= `1.9.x`

Support Time: `2023-04-27`

#### Configure

---


!!! note

    If your HDFS service version requires other special configurations, please refer to modifying the configuration file and restarting the DataCap service.

=== "Configure"

    | Field  |             Required              |             Default Value             |
    |:------:|:---------------------------------:|:-------------------------------------:|
    | `Name` | :material-check-circle: { .red }  |                   -                   |

=== "Advanced"

    |   Field    |             Required             | Description | Default Value |
    |:----------:|:--------------------------------:|:-----------:|:-------------:|
    | `file` | :material-check-circle: { .red } | `core-site.xml` <br /> `hdfs-site.xml`  |   `[]`   |

#### Version (Validation)

---

!!! warning

    The online service has not been tested yet, if you have detailed test results, please submit [issues](https://github.com/EdurtIO/datacap/issues/new/choose) to us

- [x] `3.x`
