package io.edurt.datacap.plugin.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.PluginClassLoader;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class ExamplePluginLoaderTest
{
    @Test
    public void loader()
    {
        Injector injector = Guice.createInjector(new PluginClassLoader());
        Set<Plugin> plugins = injector.getInstance(Key.get(new TypeLiteral<Set<Plugin>>() {}));
        Assert.assertTrue(plugins.size() > 0);
    }
}
