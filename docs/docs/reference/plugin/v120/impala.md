#### What is Impala ?

Apache Impala is the open source, native analytic database for Apache Hadoop.

#### Configure

---

!!! note

    When you connect to a Impala data source, you must configure the database user.

DataCap connects to Impala by default

```xml
<dependency>
    <groupId>Impala</groupId>
    <artifactId>ImpalaJDBC42</artifactId>
</dependency>
```

| Field      | Required                | Default Value |
|------------|-------------------------|---------------|
| -          | **Configure**           | -             |
| `Name`     | :material-check-circle: | -             |
| `Host`     | :material-check-circle: | -             |
| `Port`     | :material-check-circle: | 0             |
| -          | **SSL**                 | -             |
| `SSL`      | :material-check-circle: | `false`       |
| -          | **授权**                  | -             |
| `UserName` | :material-check-circle: | `root`        |

#### Version (Validation)

---

- [x] 3.2.x
