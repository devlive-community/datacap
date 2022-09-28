package io.edurt.datacap.plugin.example;

import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.PluginType;
import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExamplePlugin
        implements Plugin
{
    @Override
    public String name()
    {
        return "Example";
    }

    @Override
    public String description()
    {
        return "Example plugin";
    }

    @Override
    public PluginType type()
    {
        return null;
    }

    @Override
    public void connect(Configure configure)
    {
        log.info("Initialize plug-in connection information");
    }

    @Override
    public Response execute(String content)
    {
        log.info("Perform the plugin");
        return null;
    }

    @Override
    public void destroy()
    {
        log.info("Destroy the plugin");
    }
}
