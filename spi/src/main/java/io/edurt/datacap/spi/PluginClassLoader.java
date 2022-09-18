package io.edurt.datacap.spi;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

import java.util.ServiceLoader;

public class PluginClassLoader
        extends AbstractModule
{
    private final Iterable<PluginLoader> externalModules;

    public PluginClassLoader()
    {
        this.externalModules = ServiceLoader.load(PluginLoader.class);
    }

    public PluginClassLoader(Iterable<PluginLoader> externalModules)
    {
        this.externalModules = externalModules;
    }

    @Override
    protected void configure()
    {
        for (PluginLoader plugin : this.externalModules) {
            System.out.println(String.format("Install plugin <%s> type <%s>", plugin.getName(), plugin.getType()));
            this.install(plugin.getInstance());
        }
        Multibinder<String> binder = Multibinder.newSetBinder(this.binder(), String.class);
        binder.addBinding().toInstance(this.getClass().getSimpleName());
    }
}
