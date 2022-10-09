当我们第一次使用系统或需要添加新的数据源时，点击顶部菜单 `Admin` -> `History` 此时会跳转到查询历史管理页面，它的布局如下

![img.png](../../assets/admin/history/img.png)

默认进入页面后，显示的是添加过的所有数据源查询历史列表。

|               |        |
|---------------|--------|
| `No`          | 数据编号   |
| `Plugin`      | 调用的数据源 |
| `Create Time` | 创建时间   |
| `End Time`    | 结束时间   |
| `Elapsed`     | 总耗时    |
| `State`       | 状态     |
| `Action`      | 附加操作   |

#### 附加操作

目前附加操作包含两个:

##### 查看执行 SQL

点击 :material-eye: 按钮，将会弹出以下窗口

![img.png](../../assets/admin/history/img_1.png)

窗口内显示为本次查询里查询的具体SQL语句

##### 查看执行错误

点击 :warning: 按钮将会弹出以下窗口

![img.png](../../assets/admin/history/img_2.png)

窗口内展示本次查询具体错误信息，它只有本次查询失败情况下可用
