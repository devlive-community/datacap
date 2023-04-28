package io.edurt.datacap.spi;

import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;

public interface Plugin
{
    default String validator()
    {
        return "SELECT version() AS version";
    }

    default PluginType type()
    {
        return PluginType.JDBC;
    }

    default String name()
    {
        return this.getClass().getSimpleName().replace("Plugin", "");
    }

    default String description()
    {
        return String.format("Integrate %s data sources", this.name());
    }

    void connect(Configure configure);

    Response execute(String content);

    void destroy();
}
