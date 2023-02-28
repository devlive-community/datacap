package io.edurt.datacap.plugin.jdbc.kyuubi;

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

public class KyuubiPluginTest
{
    private Injector injector;
    private Configure configure;

    @Before
    public void before()
    {
        injector = Guice.createInjector(new KyuubiPluginModule());
        configure = new Configure();
        configure.setHost("127.0.0.1");
        configure.setPort(10009);
    }

    @Test
    public void test()
    {
        Set<Plugin> plugins = injector.getInstance(Key.get(new TypeLiteral<Set<Plugin>>() {}));
        Optional<Plugin> pluginOptional = plugins.stream()
                .filter(v -> v.name().equalsIgnoreCase("Kyuubi"))
                .findFirst();
        if (pluginOptional.isPresent()) {
            Plugin plugin = pluginOptional.get();
            plugin.connect(configure);
            System.out.println(plugin.execute("SHOW DATABASES"));
            plugin.destroy();
        }
    }
}