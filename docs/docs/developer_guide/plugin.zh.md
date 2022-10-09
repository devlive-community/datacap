---
icon: material/power-plug-outline
---

当我们需要将其他插件集成到这个系统,我们可以遵循本文中的步骤。

!!! note

    自定义插件的使用方式我们使用Kylin插件来做示例

#### 集成 `spi` 模块

在项目的 `pom.xml` 文件中添加以下依赖

```xml
<!-- 这里添加kylin jdbc相关依赖 -->
<dependency>
    <groupId>io.edurt.datacap</groupId>
    <artifactId>datacap-spi</artifactId>
    <version>DATACAP-SPI_LAST_VERSION</version>
    <scope>provided</scope>
</dependency>
```

`<build>` 和 `<properties>` 阶段一般按照规则即可

!!! warning

    `DATACAP-SPI_LAST_VERSION` 建议您将该值更改为最新的SPI的版本

#### 构建加载模块(PluginModule)

```java
package io.edurt.datacap.plugin.jdbc.kylin;

import com.google.inject.multibindings.Multibinder;
import io.edurt.datacap.spi.AbstractPluginModule;
import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.PluginModule;
import io.edurt.datacap.spi.PluginType;

public class KylinPluginModule
        extends AbstractPluginModule
        implements PluginModule
{
    @Override
    public String getName()
    {
        return "Kylin";
    }

    @Override
    public PluginType getType()
    {
        return PluginType.SOURCE;
    }

    @Override
    public AbstractPluginModule get()
    {
        return this;
    }

    protected void configure()
    {
        Multibinder<String> module = Multibinder.newSetBinder(this.binder(), String.class);
        module.addBinding().toInstance(this.getClass().getSimpleName());
        Multibinder<Plugin> plugin = Multibinder.newSetBinder(this.binder(), Plugin.class);
        plugin.addBinding().to(KylinPlugin.class);
    }
}
```

`extends AbstractPluginModule` 该模块标志着类属于一个插件

`implements PluginModule` 一些元数据实现系统所需的插件

##### `configure()` 配置

```java
Multibinder<Plugin> plugin = Multibinder.newSetBinder(this.binder(), Plugin.class);
plugin.addBinding().to(KylinPlugin.class);
```

这里的核心就是我们要将我们编写好的插件绑定并映射到系统容器中

#### 构建转换器(Adapter)

```java
package io.edurt.datacap.plugin.jdbc.kylin;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class KylinAdapter
        extends JdbcAdapter
{
    public KylinAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
```

如果没有特殊的配置只需要参照该代码即可实现，在 `JdbcAdapter` 中我们实现了具体的JDBC转换

#### 构建插件(Plugin)

```java
package io.edurt.datacap.plugin.jdbc.kylin;

import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.PluginType;
import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConfigure;
import io.edurt.datacap.spi.connection.JdbcConnection;
import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;

@Slf4j
public class KylinPlugin
        implements Plugin
{
    private JdbcConfigure jdbcConfigure;
    private JdbcConnection connection;
    private Response response;

    @Override
    public String name()
    {
        return "Kylin";
    }

    @Override
    public String description()
    {
        return "Integrate Kylin data sources";
    }

    @Override
    public PluginType type()
    {
        return PluginType.SOURCE;
    }

    @Override
    public void connect(Configure configure)
    {
        try {
            this.response = new Response();
            this.jdbcConfigure = new JdbcConfigure();
            BeanUtils.copyProperties(this.jdbcConfigure, configure);
            this.jdbcConfigure.setJdbcDriver("org.apache.kylin.jdbc.Driver");
            this.jdbcConfigure.setJdbcType("kylin");
            this.connection = new JdbcConnection(this.jdbcConfigure, this.response);
        }
        catch (Exception ex) {
            this.response.setIsConnected(Boolean.FALSE);
            this.response.setMessage(ex.getMessage());
        }
    }

    @Override
    public Response execute(String content)
    {
        if (ObjectUtils.isNotEmpty(this.connection)) {
            log.info("Execute kylin plugin logic started");
            this.response = this.connection.getResponse();
            JdbcAdapter processor = new KylinAdapter(this.connection);
            this.response = processor.handlerExecute(content);
            log.info("Execute kylin plugin logic end");
        }
        return this.response;
    }

    @Override
    public void destroy()
    {
        if (ObjectUtils.isNotEmpty(this.connection)) {
            this.connection.destroy();
        }
    }
}
```

`implements Plugin` 实现SPI系统中定义的插件, 特定插件实现的内容。

!!! note

    `name()` 插件的唯一名称,名称相同的插件只会生效第一次加载的 <br />
    `type()` 插件的类型 <br />
    `description()` 插件的描述<br />
    `connect(Configure configure)` 插件事先需要的连接相关信息，如Kylin插件，这就是Kylin的连接阶段（系统预置了JDBC连接方式直接使用即可）。 <br/>
    `execute(String content)` 特定的执行操作逻辑 <br/>
    `destroy()` 对于插件的最终销毁，请注意需要在连接中包含信息的销毁

需要注意的是在 `connect(Configure configure)` 代码中我们需要指定连接器的 driver 和 type, 在代码中我们重置了 `JdbcConfigure` 也就是具体的JDBC配置信息
系统会根据指定的 driver 加载它，根据 type 进行生成格式为 `jdbc:<type>//xxxxxx` 连接配置，如果是非jdbc的插件需要自己实现连接方式。

在 `execute(String content)` 阶段我们通过实现系统定制的 `JdbcAdapter` 转换器将会自动进行数据装配已经封装等，如果是非jdbc的插件需要自己实现转换器。
    
#### 构建SPI配置插件

在 `resources` 源码目录下添加 `META-INF` 和 `services` 目录

!!! warning

    `services` 需要在 `resources` 目录中

创建 `io.edurt.datacap.spi.PluginModule` 文件

```java
io.edurt.datacap.plugin.jdbc.kylin.KylinPluginModule
```

文件的内容是我们定义之后的插件加载模块。

!!! warning

    插件的单元测试可以参考已经发布的插件进行测试
