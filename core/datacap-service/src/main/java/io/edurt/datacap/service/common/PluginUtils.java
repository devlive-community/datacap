package io.edurt.datacap.service.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Preconditions;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import io.edurt.datacap.common.utils.JsonUtils;
import io.edurt.datacap.common.utils.ResourceUtils;
import io.edurt.datacap.service.configure.IConfigure;
import io.edurt.datacap.spi.Plugin;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.io.File;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class PluginUtils
{
    private static final ObjectMapper yamlFactory = new ObjectMapper(new YAMLFactory());

    private PluginUtils() {}

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

    @Deprecated
    public static IConfigure loadConfigure(String type, String plugin, String resource, Environment environment)
    {
        String root = environment.getProperty("spring.config.location");
        if (!resource.endsWith(".json")) {
            resource = resource + ".json";
        }
        String path = root + String.format("plugins/%s/%s", type.toLowerCase(), resource.toLowerCase());
        log.info("Load plugin {} type {} resource {} configure file path {}", plugin, type, resource, path);
        String json = ResourceUtils.loadResourceToString(path, true);
        if (StringUtils.isEmpty(json)) {
            log.warn("Plugin {} type {} configuration file {} not found, load default configuration file", plugin, type, resource);
            json = ResourceUtils.loadResourceToString(root + "plugins/default.json", true);
        }
        Preconditions.checkArgument(StringUtils.isNotEmpty(json), "Invalid configuration file, it may be a bug, please submit issues to solve it.");
        IConfigure configure = null;
        try {
            configure = JsonUtils.objectmapper.readValue(json, IConfigure.class);
        }
        catch (JsonProcessingException e) {
            log.error("Format configuration file, it may be a bug, please submit issues to solve it. plugin {} type {} resource {} configure file path {} message ", plugin, type, resource, path, e);
            Preconditions.checkArgument(StringUtils.isNotEmpty(json), "Format configuration file, it may be a bug, please submit issues to solve it.");
        }
        return configure;
    }

    public static IConfigure loadYamlConfigure(String type, String plugin, String resource, Environment environment)
    {
        String root = environment.getProperty("spring.config.location");
        if (resource.contains(" ")) {
            resource = resource.split(" ")[0];
        }
        if (!resource.endsWith(".yaml")) {
            resource = resource + ".yaml";
        }
        String path = root + String.format("plugins/%s/%s", type.toLowerCase(), resource.toLowerCase());
        File file = new File(path);
        if (!file.exists()) {
            log.warn("Plugin {} type {} configuration file {} not found, load default configuration file", plugin, type, resource);
            file = new File(root + "plugins/default.yaml");
        }
        else {
            log.info("Load plugin {} type {} resource {} configure file path {}", plugin, type, resource, path);
        }
        yamlFactory.findAndRegisterModules();
        IConfigure configure = null;
        try {
            configure = yamlFactory.readValue(file, IConfigure.class);
        }
        catch (Exception e) {
            log.error("Format configuration file, it may be a bug, please submit issues to solve it. plugin {} type {} resource {} configure file path {} message ", plugin, type, resource, path, e);
            Preconditions.checkArgument(StringUtils.isNotEmpty(path), "Format configuration file, it may be a bug, please submit issues to solve it.");
        }
        return configure;
    }
}
