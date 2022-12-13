---
icon: material/power-plug-outline
---

When we need to integrate other plugins into this system, we can follow the steps in this article.

!!! note

    How custom plugins are used We use the Kylin plugin as an example

#### Integrated `spi` module

Add the following dependencies in the project's `pom.xml` file

```xml
<!-- Add kylin jdbc related dependencies here -->
<dependency>
    <groupId>io.edurt.datacap</groupId>
    <artifactId>datacap-spi</artifactId>
    <version>DATACAP-SPI_LAST_VERSION</version>
    <scope>provided</scope>
</dependency>
```

`<build>` and `<properties>` The stage is generally in accordance with the rules

!!! warning

    `DATACAP-SPI_LAST_VERSION` It is recommended that you change this value to the latest SPI version

#### Build Load Module (PluginModule)

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

`extends AbstractPluginModule` The module indicates that the class belongs to a plugin

`implements PluginModule` Some metadata implements the plug-ins required by the system

##### `configure()`

```java
Multibinder<Plugin> plugin = Multibinder.newSetBinder(this.binder(), Plugin.class);
plugin.addBinding().to(KylinPlugin.class);
```

The core here is that we want to bind and map the plugin we wrote into the system container

#### Build Adapter

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

If there is no special configuration, just refer to the code to implement it, in `JdbcAdapter` we implement the specific JDBC conversion

#### Build Plugin

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

`implements Plugin` Implement the plug-in defined in the SPI system, what the specific plug-in implements.

!!! note

    `name()` The plug-in has a unique name, and plug-ins with the same name will only take effect the first time they are loaded <br />
    `type()` The type of plug-in <br />
    `description()` The description of the plug-in<br />
    `connect(Configure configure)` The plug-in needs connection information in advance, such as the Kylin plugin, which is the connection stage of Kylin (the system presets the JDBC connection method to use it directly). <br/>
    `execute(String content)` Specific execution operation logic <br/>
    `destroy()` For the final destruction of the plug-in, note that the destruction needs to include information in the connection

Note that in the `connect(Configure configure)` code we need to specify the driver and type of the connector, in the code we reset the `JdbcConfigure` that is, the specific JDBC configuration information
The system will load it according to the specified driver, generate the format according to type as `jdbc:<type>xxxxxx` connection configuration, if it is a non-jdbc plug-in needs to implement the connection mode itself.

In the `execute(String content)` stage we implement the system customization of the `JdbcAdapter` converter will automatically perform data assembly already packaged, etc., if it is a non-jdbc plug-in need to implement the converter itself.

#### Build the SPI configuration plugin

Add the `META-INF` and `services` directories under the `resources` source directory

!!! warning

    `services` Required in the `resources` directory

Create the `io.edurt.datacap.spi.PluginModule` file

```java
io.edurt.datacap.plugin.jdbc.kylin.KylinPluginModule
```

The contents of the file are the plug-in loading modules that we have defined.

!!! warning

    Unit tests of plug-ins can be tested with reference to plugins that have already been released
