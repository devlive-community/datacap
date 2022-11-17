CrateDB 是一个分布式SQL数据库，它可以方便地实时存储和分析大量的机器数据。

#### 配置

---

!!! note

    对于CrateDB版本 `2.1.x` 及更高版本，连接时必须配置数据库用户。

DataCap 连接 CrateDB 默认使用的是

```xml
<dependency>
    <groupId>io.crate</groupId>
    <artifactId>crate-jdbc</artifactId>
</dependency>
```

| Field  | Required                | Default Value |
|--------|-------------------------|---------------|
| -      | **Configure**           | -             |
| `Name` | :material-check-circle: | -             |
| `Host` | :material-check-circle: | -             |
| `Port` | :material-check-circle: | 0             |
| -      | **SSL**                 | -             |
| `SSL`  | :material-check-circle: | `false`       |
| -      | **授权**                  | -             |
| `用户名`  | :material-check-circle: | `crate`       |

#### 版本

---

- [x] 4.5.x
