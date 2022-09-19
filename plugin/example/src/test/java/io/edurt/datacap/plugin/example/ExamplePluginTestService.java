package io.edurt.datacap.plugin.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.PluginLoader;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class ExamplePluginTestService
{
    @Test
    public void loader()
    {
        Injector injector = Guice.createInjector(new PluginLoader());
        Set<Plugin> plugins = injector.getInstance(Key.get(new TypeLiteral<Set<Plugin>>() {}));
        Assert.assertTrue(plugins.size() > 0);
    }
}
