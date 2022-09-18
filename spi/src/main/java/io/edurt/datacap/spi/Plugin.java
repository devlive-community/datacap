package io.edurt.datacap.spi;

import com.google.inject.AbstractModule;

public interface Plugin
{
    String getName();

    PluginEnum getType();

    AbstractPlugin getInstance();
}

class AbstractPlugin
        extends AbstractModule
{
}
