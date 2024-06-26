package io.edurt.datacap.plugin.influxdb

import com.google.inject.Guice.createInjector
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.TypeLiteral
import io.edurt.datacap.spi.Plugin
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class InfluxDBModuleTest
{
    private val name = "InfluxDB"
    private var injector: Injector? = null

    @Before
    fun before()
    {
        injector = createInjector(InfluxDBModule())
    }

    @Test
    fun test()
    {
        injector !!.getInstance(Key.get(object : TypeLiteral<Set<Plugin>>()
        {}))
            .stream()
            .findFirst()
            .ifPresent { assertEquals(name, it.name()) }
    }
}
