package io.edurt.datacap.spi;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PluginLoader
        extends AbstractModule
{
    private final Iterable<Plugin> externalModules;

    public PluginLoader(Iterable<Plugin> externalModules)
    {
        this.externalModules = externalModules;
    }

    @Override
    protected void configure()
    {
        for (Plugin plugin : this.externalModules) {
            System.out.println(String.format("Install plugin <%s> type <%s>", plugin.getName(), plugin.getType()));
            this.install(plugin.getInstance());
        }
        Multibinder<String> binder = Multibinder.newSetBinder(this.binder(), String.class);
        binder.addBinding().toInstance(this.getClass().getSimpleName());
    }
}
