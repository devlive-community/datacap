package io.edurt.datacap.plugin.example;

import com.google.inject.multibindings.Multibinder;
import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.PluginDestination;
import io.edurt.datacap.spi.PluginModule;
import io.edurt.datacap.spi.PluginType;

public class ExamplePluginModule
        extends PluginModule
        implements Plugin
{
    @Override
    public String getName()
    {
        return this.getClass().getName();
    }

    @Override
    public PluginType getType()
    {
        return PluginType.SOURCE;
    }

    @Override
    public PluginModule getInstance()
    {
        return this;
    }

    protected void configure()
    {
        Multibinder<String> module = Multibinder.newSetBinder(this.binder(), String.class);
        module.addBinding().toInstance(this.getClass().getSimpleName());
        Multibinder<PluginDestination> plugin = Multibinder.newSetBinder(this.binder(), PluginDestination.class);
        plugin.addBinding().to(ExamplePlugin.class);
    }
}
