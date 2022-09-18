package io.edurt.datacap.spi;

public interface PluginLoader
{
    String getName();

    PluginEnum getType();

    AbstractPlugin getInstance();
}
