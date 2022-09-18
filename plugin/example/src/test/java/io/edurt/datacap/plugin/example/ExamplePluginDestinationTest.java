package io.edurt.datacap.plugin.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import io.edurt.datacap.spi.PluginDestination;
import io.edurt.datacap.spi.PluginLoader;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class ExamplePluginDestinationTest
{
    @Test
    public void loader()
    {
        Injector injector = Guice.createInjector(new PluginLoader());
        Set<PluginDestination> plugins = injector.getInstance(Key.get(new TypeLiteral<Set<PluginDestination>>() {}));
        Assert.assertTrue(plugins.size() > 0);
    }
}
