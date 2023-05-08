package io.edurt.datacap.plugin.mongo

import com.google.common.collect.Lists.newArrayList
import com.google.inject.Guice.createInjector
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.TypeLiteral
import io.edurt.datacap.spi.Plugin
import io.edurt.datacap.spi.model.Configure
import io.edurt.datacap.spi.model.Response
import org.apache.commons.lang3.ObjectUtils.isNotEmpty
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory.getLogger
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.lifecycle.Startables.deepStart
import java.lang.String.format
import java.util.stream.Stream

class MongoPluginTest {
    private val log = getLogger(this::class.java)

    private var injector: Injector? = null
    private var configure: Configure? = null
    private var container: MongoContainer? = null

    @Before
    fun before() {
        log.info("MongoBD container initialized")
        container = MongoContainer()
            .waitingFor(Wait.forHttp("/"))
        container?.portBindings = newArrayList(format("%s:%s", MongoContainer.PORT, MongoContainer.PORT))
        deepStart(Stream.of(container)).join()
        log.info("MongoBD container started")

        injector = createInjector(MongoModule())
        configure = Configure()
        configure?.host = "localhost"
        configure?.port = MongoContainer.PORT
    }

    @Test
    fun test() {
        val plugin: Plugin? = injector?.getInstance(Key.get(object : TypeLiteral<Set<Plugin?>?>() {}))
            ?.first { v -> v?.name().equals("MongoDB Community") }
        if (isNotEmpty(plugin)) {
            plugin?.connect(configure)
            val sql = "select * from system.version"
            val response: Response = plugin!!.execute(sql)
            log.info("================ Plugin execution information ================")
            if (!response.isSuccessful) {
                log.error("Message: {}", response.message)
            } else {
                response.columns.forEach { column -> log.info(column.toString()) }
            }
            assertTrue(response.isSuccessful)
        }
    }
}
