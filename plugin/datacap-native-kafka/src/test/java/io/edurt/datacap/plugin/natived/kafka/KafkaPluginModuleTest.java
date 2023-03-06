package io.edurt.datacap.plugin.natived.kafka;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import io.edurt.datacap.spi.Plugin;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

public class KafkaPluginModuleTest
{
    private Injector injector;

    @Before
    public void before()
    {
        this.injector = Guice.createInjector(new KafkaPluginModule());
    }

    @Test
    public void test()
    {
        Set<Plugin> plugins = injector.getInstance(Key.get(new TypeLiteral<Set<Plugin>>() {}));
        Assert.assertTrue(plugins.size() > 0);
    }
}