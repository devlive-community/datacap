package io.edurt.datacap.server.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Preconditions;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import io.edurt.datacap.server.plugin.configure.IConfigure;
import io.edurt.datacap.spi.Plugin;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.util.Optional;
import java.util.Set;

@Slf4j
public class PluginCommon
{
    private PluginCommon() {}

    public static Optional<Plugin> getPluginByName(Injector injector, String pluginName)
    {
        Optional<Plugin> pluginOptional = injector.getInstance(Key.get(new TypeLiteral<Set<Plugin>>() {})).stream().filter(plugin -> plugin.name().equalsIgnoreCase(pluginName)).findFirst();
        return pluginOptional;
    }

    public static Optional<Plugin> getPluginByNameAndType(Injector injector, String pluginName, String pluginType)
    {
        Optional<Plugin> pluginOptional = injector.getInstance(Key.get(new TypeLiteral<Set<Plugin>>() {})).stream().filter(plugin -> plugin.name().equalsIgnoreCase(pluginName) && plugin.type().name().equalsIgnoreCase(pluginType)).findFirst();
        return pluginOptional;
    }

    public static IConfigure loadConfigure(String type, String plugin, String resource, Environment environment)
    {
        String root = environment.getProperty("spring.config.location");
        if (!resource.endsWith(".json")) {
            resource = resource + ".json";
        }
        String path = root + String.format("plugins/%s/%s", type.toLowerCase(), resource.toLowerCase());
        log.info("Load plugin {} type {} resource {} configure file path {}", plugin, type, resource, path);
        String json = ResourceCommon.loadResourceToString(path, true);
        if (StringUtils.isEmpty(json)) {
            log.warn("Plugin {} type {} configuration file {} not found, load default configuration file", plugin, type, resource);
            json = ResourceCommon.loadResourceToString(root + "plugins/default.json", true);
        }
        Preconditions.checkArgument(StringUtils.isNotEmpty(json), "Invalid configuration file, it may be a bug, please submit issues to solve it.");
        IConfigure configure = null;
        try {
            configure = JSON.objectmapper.readValue(json, IConfigure.class);
        }
        catch (JsonProcessingException e) {
            log.error("Format configuration file, it may be a bug, please submit issues to solve it. plugin {} type {} resource {} configure file path {} message ", plugin, type, resource, path, e);
            Preconditions.checkArgument(StringUtils.isNotEmpty(json), "Format configuration file, it may be a bug, please submit issues to solve it.");
        }
        return configure;
    }
}
