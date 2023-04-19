package io.edurt.datacap.plugin.jdbc.doris

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import io.edurt.datacap.spi.Plugin;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

class DorisPluginModuleTest {
    private var injector: Injector? = null

    @Before
    fun before() {
        injector = Guice.createInjector(DorisPluginModule())
    }

    @Test
    fun test() {
        val plugins: Set<Plugin?>? = injector?.getInstance(Key.get(object : TypeLiteral<Set<Plugin?>?>() {}))
        Assert.assertTrue(plugins!!.size > 0)
    }
}
