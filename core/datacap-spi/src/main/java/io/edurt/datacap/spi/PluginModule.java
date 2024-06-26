package io.edurt.datacap.spi;

public interface PluginModule
{
    default String getName()
    {
        return this.getClass().getSimpleName()
                .replace("PluginModule", "")
                .replace("Module", "");
    }

    default PluginType getType()
    {
        return PluginType.JDBC;
    }

    AbstractPluginModule get();
}
