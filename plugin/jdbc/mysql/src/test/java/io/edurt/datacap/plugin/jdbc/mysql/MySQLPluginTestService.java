package io.edurt.datacap.plugin.jdbc.mysql;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.model.Configure;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.Set;

public class MySQLPluginTestService
{
    private Injector injector;
    private Configure configure;

    @Before
    public void before()
    {
        injector = Guice.createInjector(new MySQLPluginModule());
        configure = new Configure();
        configure.setHost("localhost");
        configure.setPort(3306);
        configure.setUsername(Optional.of("root"));
        configure.setPassword(Optional.of("12345678"));
    }

    @Test
    public void test()
    {
        Set<Plugin> plugins = injector.getInstance(Key.get(new TypeLiteral<Set<Plugin>>() {}));
        Optional<Plugin> pluginOptional = plugins.stream().filter(v -> v.name().equalsIgnoreCase("MySQL")).findFirst();
        if (pluginOptional.isPresent()) {
            Plugin plugin = pluginOptional.get();
            plugin.connect(configure);
            System.out.println(plugin.execute("SELECT * FROM bootstack.system_interface LIMIT 100"));
            plugin.destroy();
        }
    }
}
