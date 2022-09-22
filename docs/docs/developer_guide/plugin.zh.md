---
icon: material/power-plug-outline
---

当我们需要将其他插件集成到这个系统,我们可以遵循本文中的步骤。

- 使用项目集成 `spi` 模块

在项目的 `pom.xml` 文件中添加以下依赖

```xml
<dependency>
    <groupId>io.edurt.datacap</groupId>
    <artifactId>datacap-spi</artifactId>
    <version>DATACAP-SPI_LAST_VERSION</version>
</dependency>
```

- 创建一个插件加载模块 `ExamplePluginModule`

!!! warning

    `DATACAP-SPI_LAST_VERSION` 建议您将该值更改为最新的SPI的版本

```java
public class ExamplePluginModule
        extends AbstractPluginModule
        implements PluginModule
{
    @Override
    public String getName()
    {
        return "Example";
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
        plugin.addBinding().to(ExamplePlugin.class);
    }
}
```

`extends AbstractPluginModule` 该模块标志着类属于一个插件

`implements PluginModule` 一些元数据实现系统所需的插件

- 接下来我们需要构建插件 `ExamplePlugin`

```java
public class ExamplePlugin
        implements Plugin
{
    @Override
    public String getName()
    {
        return null;
    }

    @Override
    public PluginType getType()
    {
        return null;
    }

    @Override
    public void connect(Configure configure)
    {
    }

    @Override
    public Response execute(String content)
    {
        return null;
    }

    @Override
    public void destroy()
    {
    }
}
```

`implements Plugin` 实现SPI系统中定义的插件, 特定插件实现的内容。

!!! note

    `getName()` 插件的唯一名称,名称相同的插件只会生效第一次加载的 <br/>
    `getType()` 插件的类型 <br/>
    `connect(Configure configure)` 插件事先需要的连接相关信息，如MySQL插件，这就是MySQL的连接阶段。 <br/>
    `execute(String content)` 特定的执行操作逻辑 <br/>
    `destroy()` 对于插件的最终销毁，请注意需要在连接中包含信息的销毁
    
- 在最后一步中，我们加载并配置插件

在 `resources` 源码目录下添加 `META-INF` 和 `services` 目录

!!! warning

    `services` 需要在 `resources` 目录中

创建 `io.edurt.datacap.spi.PluginModule` 文件

```java
io.edurt.datacap.plugin.example.ExamplePluginModule
```

文件的内容是我们定义之后的插件加载模块。
