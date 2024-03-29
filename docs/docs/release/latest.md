**DataCap 发布!**

|    发布版本     |     发布时间     |
|:-----------:|:------------:|
| `2024.02.1` | `2024-03-01` |

#### Plugin

---

- 支持 ParadeDB (https://github.com/devlive-community/datacap/issues/589)
- 支持 TimescaleDB (https://github.com/devlive-community/datacap/issues/593)

#### DataSet

---

- [图表] 添加面积图 (https://github.com/devlive-community/datacap/issues/611)
- [图表] 添加多面积图 (https://github.com/devlive-community/datacap/issues/610)
- [图表] 添加饼图 (https://github.com/devlive-community/datacap/issues/613)
- [图表] 支持折线图断点处理
- [图表] 支持分组直方图 (https://github.com/devlive-community/datacap/issues/612)
- [图表] 支持词云图 (https://github.com/devlive-community/datacap/issues/614)
- 支持添加主键 (https://github.com/devlive-community/datacap/issues/624)
- 添加`总行数`和`总大小` (https://github.com/devlive-community/datacap/pull/676)
- 支持自定义别名的即席查询 (https://github.com/devlive-community/datacap/issues/615)
- 支持维度排序
- 支持指标排序
- 支持数据采样列 (https://github.com/devlive-community/datacap/issues/605)
- 添加有符号号码类型
- 修复了小屏幕上列表中的显示问题
- 支持即席查询自定义函数 (https://github.com/devlive-community/datacap/issues/603)
- 添加筛选器分组
- 合并查询列
- 修复复制数据导致重复请求的问题
- 修复编辑报表分组异常问题
- 修复无限循环查询问题
- 支持过滤器 `IS NULL`， `IS NOT NULL`
- 修复了多列查询导致 expres 混淆的问题
- 支持过滤器 `LIKE`， `NOT LIKE`
- 支持过滤器 `=`， `<>`
- 支持过滤器 `>`， `>=`
- 支持过滤器 `<`， `<=`
- 支持历史错误信息同步
- 支持清空数据 (https://github.com/devlive-community/datacap/issues/622)
- 支持动态添加列 (https://github.com/devlive-community/datacap/issues/623)
- 重构数据集详细信息页面
- 添加日期时间类型
- 支持自定义生命周期 (https://github.com/devlive-community/datacap/issues/616)
- 禁用 `BOOLEAN`、`DATETIME` 类型分配
- 支持虚拟列 (https://github.com/devlive-community/datacap/issues/602)
- 支持docker compose (https://github.com/devlive-community/datacap/issues/648)
- 支持七牛云存储 (https://github.com/devlive-community/datacap/issues/618)

#### Dependencies

---

- 更新 io.crate:crate-jdbc `2.6.0` 到 `2.7.0`
- 更新 Impala:ImpalaJDBC42 `2.6.29.1035` 到 `2.6.32.1041` 
- 更新 org.yaml:snakeyaml `2.0` 到 `2.2`
- 更新 org.apache.hive:hive-jdbc `3.1.2` 到 `3.1.3`
- 更新 org.elasticsearch.plugin:x-pack-sql-jdbc `8.11.3` 到 `8.12.0`
- 更新 jackson.version `2.16.0` 到 `2.16.1`
- 更新 org.apache.maven.plugins:maven-javadoc-plugin `3.6.0` 到 `3.6.3`
- 更新 com.microsoft.sqlserver:mssql-jdbc `11.2.1.jre8` 到 `12.6.0.jre8`
- 更新 postgresql.version `42.6.0` 到 `42.7.2`
