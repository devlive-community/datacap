package io.edurt.datacap.plugin.timescale

import com.google.inject.Guice.createInjector
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.TypeLiteral
import io.edurt.datacap.spi.Plugin
import org.apache.commons.lang3.ObjectUtils.isNotEmpty
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

class TimescaleModuleTest {
    private val name = "Timescale"
    private var injector: Injector? = null

    @Before
    fun before() {
        injector = createInjector(TimescaleModule())
    }

    @Test
    fun test() {
        val plugin: Plugin? = injector?.getInstance(Key.get(object : TypeLiteral<Set<Plugin?>?>() {}))
                ?.first { v -> v?.name().equals(name) }
        assertTrue(isNotEmpty(plugin))
    }
}
