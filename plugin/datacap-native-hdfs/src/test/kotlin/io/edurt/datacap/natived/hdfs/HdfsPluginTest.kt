package io.edurt.datacap.natived.hdfs

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.TypeLiteral
import io.edurt.datacap.spi.Plugin
import io.edurt.datacap.spi.model.Configure
import io.edurt.datacap.spi.model.Response
import org.apache.commons.lang3.ObjectUtils
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory.getLogger
import java.util.*

class HdfsPluginTest {
    private val log = getLogger(this::class.java)

    private var injector: Injector? = null
    private var configure: Configure? = null

    @Before
    fun before() {
        injector = Guice.createInjector(HdfsModule())
        configure = Configure()
        configure?.home = this.javaClass.getResource("/").file.toString()
        configure?.username = Optional.of("default")
    }

    @Test
    fun test() {
        val plugin: Plugin? = injector?.getInstance(Key.get(object : TypeLiteral<Set<Plugin?>?>() {}))
            ?.first { v -> v?.name().equals("Hdfs") }
        if (ObjectUtils.isNotEmpty(plugin)) {
            plugin?.connect(configure)
            val sql = "SHOW DATABASES"
            val response: Response = plugin!!.execute(sql)
            log.info("================ plugin executed information =================")
            if (!response.isSuccessful) {
                log.error("Message: {}", response.message)
            } else {
                response.columns.forEach { column -> log.info(column.toString()) }
            }
            Assert.assertTrue(response.isSuccessful)
        }
    }
}
