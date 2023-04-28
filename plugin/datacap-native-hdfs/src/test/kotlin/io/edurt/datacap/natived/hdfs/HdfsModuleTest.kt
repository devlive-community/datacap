package io.edurt.datacap.natived.hdfs

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.TypeLiteral
import io.edurt.datacap.spi.Plugin
import org.apache.commons.lang3.ObjectUtils
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class HdfsModuleTest {
    private var injector: Injector? = null

    @Before
    fun before() {
        injector = Guice.createInjector(HdfsModule())
    }

    @Test
    fun test() {
        val plugin: Plugin? = injector?.getInstance(Key.get(object : TypeLiteral<Set<Plugin?>?>() {}))
            ?.first { v -> v?.name().equals("Hdfs") }
        Assert.assertTrue(ObjectUtils.isNotEmpty(plugin))
    }
}