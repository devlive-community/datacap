package io.edurt.datacap.plugin.jdbc.snowflake;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.model.Configure;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.Set;

public class SnowflakePluginTest
{
    private Injector injector;
    private Configure configure;

    @Before
    public void before()
    {
        injector = Guice.createInjector(new SnowflakePluginModule());
        configure = new Configure();
        configure.setHost("127.0.0.1");
        configure.setPort(80);
        configure.setUsername(Optional.of("root"));
        configure.setPassword(Optional.of("snowflake"));
    }

    @Test
    public void test()
    {
        Set<Plugin> plugins = injector.getInstance(Key.get(new TypeLiteral<Set<Plugin>>() {}));
        Optional<Plugin> pluginOptional = plugins.stream()
                .filter(v -> v.name().equalsIgnoreCase("Snowflake"))
                .findFirst();
        if (pluginOptional.isPresent()) {
            Plugin plugin = pluginOptional.get();
            plugin.connect(configure);
            Assert.assertNotNull(plugin.execute(plugin.validator()).getConnection());
            plugin.destroy();
        }
    }
}