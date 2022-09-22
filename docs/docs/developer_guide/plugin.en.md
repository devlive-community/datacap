---
icon: material/power-plug-outline
---

When we need to integrate other plug-ins into this system, we can follow the steps in this document.

- Use the project to integrate the `spi` module

Add the following dependencies to the project's `pom.xml` file

```xml
<dependency>
    <groupId>io.edurt.datacap</groupId>
    <artifactId>datacap-spi</artifactId>
    <version>DATACAP-SPI_LAST_VERSION</version>
</dependency>
```

- Create a plug-in loading module `ExamplePluginModule`

!!! warning

    `DATACAP-SPI_LAST_VERSION`  You are advised to change the value to the latest SPI version

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

`extends AbstractPluginModule` The module that marks the class as belonging to a plug-in

`implements PluginModule` Some metadata needed to implement the system plug-in

- Next we need to build the plugin we use `ExamplePlugin`

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

`implements Plugin` Implement the plug-ins defined in the SPI system, and mark the content of the specific plug-in implementation.

!!! note

    `getName()` The unique name of the plug-in, the plug-in with the same name will only take effect if it is loaded first <br/>
    `getType()` Types of plug-ins <br/>
    `connect(Configure configure)` The connection-related information required by the plug-in in advance, such as the MySQL plug-in, this is the connection stage of MySQL. <br/>
    `execute(String content)` Specific execution operation logic <br/>
    `destroy()` For the final destruction of the plug-in, pay attention to the need to include the destruction of the information in the connection
    
- In the last step, we load and configure the plug-in

Add the `META-INF` and `services` directories to the `resources` directory in the source directory.

!!! warning

    `services` needs to be in the `META-INF` directory

Create `io.edurt.datacap.spi.PluginModule` file

```java
io.edurt.datacap.plugin.example.ExamplePluginModule
```

The content of the file is the plug-in loading module after we define it.
