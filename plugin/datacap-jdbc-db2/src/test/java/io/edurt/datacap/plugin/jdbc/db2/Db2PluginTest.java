package io.edurt.datacap.plugin.jdbc.db2;

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

public class Db2PluginTest
{
    private Injector injector;
    private Configure configure;

    @Before
    public void before()
    {
        injector = Guice.createInjector(new Db2PluginModule());
        configure = new Configure();
        configure.setHost("127.0.0.1");
        configure.setPort(50000);
        configure.setUsername(Optional.ofNullable("default"));
        configure.setPassword(Optional.ofNullable("12345678"));
        configure.setDatabase(Optional.ofNullable("default"));
    }

    @Test
    public void test()
    {
        Set<Plugin> plugins = injector.getInstance(Key.get(new TypeLiteral<Set<Plugin>>() {}));
        Optional<Plugin> pluginOptional = plugins.stream()
                .filter(v -> v.name().equalsIgnoreCase("DB2"))
                .findFirst();
        if (pluginOptional.isPresent()) {
            Plugin plugin = pluginOptional.get();
            plugin.connect(configure);
            Assert.assertNotNull(plugin.execute("select tabname from syscat.tables LIMIT 1")
                    .getConnection());
            plugin.destroy();
        }
    }
}
