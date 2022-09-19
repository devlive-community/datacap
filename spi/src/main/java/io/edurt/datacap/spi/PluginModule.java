package io.edurt.datacap.spi;

public interface PluginModule
{
    String getName();

    PluginType getType();

    AbstractPluginModule get();
}
