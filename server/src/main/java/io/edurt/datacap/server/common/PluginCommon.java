package io.edurt.datacap.server.common;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import io.edurt.datacap.spi.Plugin;

import java.util.Optional;
import java.util.Set;

public class PluginCommon
{
    private PluginCommon() {}

    public static Optional<Plugin> getPluginByName(Injector injector, String pluginName)
    {
        Optional<Plugin> pluginOptional = injector.getInstance(Key.get(new TypeLiteral<Set<Plugin>>() {}))
                .stream()
                .filter(plugin -> plugin.name().equalsIgnoreCase(pluginName))
                .findFirst();
        return pluginOptional;
    }
}
