package io.edurt.datacap.spi;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

@Slf4j
public class PluginLoader
        extends AbstractModule
{
    private final Iterable<PluginModule> externalModules;
    private final List<PluginModule> modules = new ArrayList<>();

    public PluginLoader()
    {
        this.externalModules = ServiceLoader.load(PluginModule.class);
    }

    public PluginLoader(Iterable<PluginModule> externalModules)
    {
        this.externalModules = externalModules;
    }

    public void addPlugin(Iterable<PluginModule> externalModules)
    {
        externalModules.forEach(v -> log.debug("Add plugin {}", v));
        for (PluginModule plugin : externalModules) {
            modules.add(plugin);
        }
    }

    @Override
    protected void configure()
    {
        log.info("Loading plugin start ...");
        for (PluginModule plugin : this.externalModules) {
            this.modules.add(plugin);
        }
        for (PluginModule plugin : this.modules) {
            log.info("Install plugin <{}> type <{}>", plugin.getName(), plugin.getType());
            this.install(plugin.get());
        }
        Multibinder<String> binder = Multibinder.newSetBinder(this.binder(), String.class);
        binder.addBinding().toInstance(this.getClass().getSimpleName());
        log.info("Loading plugin end ...");
    }
}
