package io.edurt.datacap.plugin.jdbc.trino;

import com.google.inject.multibindings.Multibinder;
import io.edurt.datacap.spi.AbstractPluginModule;
import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.PluginModule;
import io.edurt.datacap.spi.PluginType;

public class TrinoPluginModule
        extends AbstractPluginModule
        implements PluginModule
{
    @Override
    public String getName()
    {
        return "Trino";
    }

    @Override
    public PluginType getType()
    {
        return PluginType.JDBC;
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
        plugin.addBinding().to(TrinoPlugin.class);
    }
}
