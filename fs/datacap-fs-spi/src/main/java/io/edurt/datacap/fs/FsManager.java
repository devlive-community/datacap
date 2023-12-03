package io.edurt.datacap.fs;

import com.google.inject.AbstractModule;
import lombok.extern.slf4j.Slf4j;

import java.util.ServiceLoader;

@Slf4j
public class FsManager
        extends AbstractModule
{
    private final Iterable<FsModule> externalModules;

    public FsManager()
    {
        this.externalModules = ServiceLoader.load(FsModule.class);
    }

    @Override
    protected void configure()
    {
        log.info("Loading fs start ...");
        for (FsModule module : this.externalModules) {
            log.info("Install fs [ {} ]", module.name());
            this.install(module);
        }
        log.info("Loading fs end ...");
    }
}
