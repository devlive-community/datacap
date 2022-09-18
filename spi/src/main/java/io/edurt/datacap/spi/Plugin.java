package io.edurt.datacap.spi;

public interface Plugin
{
    String getName();

    PluginType getType();

    PluginModule getInstance();
}
