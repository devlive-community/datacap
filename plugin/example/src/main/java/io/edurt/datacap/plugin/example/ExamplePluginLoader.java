package io.edurt.datacap.plugin.example;

import com.google.inject.multibindings.Multibinder;
import io.edurt.datacap.spi.AbstractPlugin;
import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.PluginEnum;
import io.edurt.datacap.spi.PluginLoader;

public class ExamplePluginLoader
        extends AbstractPlugin
        implements PluginLoader
{
    @Override
    public String getName()
    {
        return this.getClass().getName();
    }

    @Override
    public PluginEnum getType()
    {
        return PluginEnum.SOURCE;
    }

    @Override
    public AbstractPlugin getInstance()
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
