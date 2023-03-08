package io.edurt.datacap.spi.executor;

import com.google.inject.AbstractModule;
import lombok.extern.slf4j.Slf4j;

import java.util.ServiceLoader;

@Slf4j
public class ExecutorManager
        extends AbstractModule
{
    private final Iterable<ExecutorModule> externalModules;

    public ExecutorManager()
    {
        this.externalModules = ServiceLoader.load(ExecutorModule.class);
    }

    public ExecutorManager(Iterable<ExecutorModule> externalModules)
    {
        this.externalModules = externalModules;
    }

    @Override
    protected void configure()
    {
        log.info("Loading executor start ...");
        for (ExecutorModule plugin : this.externalModules) {
            log.info("Install executor <{}>", plugin.getClass().getSimpleName());
            this.install(plugin);
        }
        log.info("Loading executor end ...");
    }
}
