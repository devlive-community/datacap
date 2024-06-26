package io.edurt.datacap.plugin.influxdb

import com.google.common.collect.Lists
import com.google.inject.Guice.createInjector
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.TypeLiteral
import io.edurt.datacap.file.FileManager
import io.edurt.datacap.spi.Plugin
import io.edurt.datacap.spi.model.Configure
import io.edurt.datacap.spi.model.Response
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory.getLogger
import org.testcontainers.containers.Network
import org.testcontainers.lifecycle.Startables
import org.testcontainers.shaded.org.awaitility.Awaitility.given
import java.util.*
import java.util.concurrent.TimeUnit

class InfluxDBPluginTest
{
    private val log = getLogger(this.javaClass)
    private val host = "TestInfluxDBContainer"
    private var container: InfluxDBContainer? = null
    private var injector: Injector? = null
    private var configure: Configure? = null

    @Before
    fun before()
    {
        val network = Network.newNetwork()
        container = InfluxDBContainer()
            .withNetwork(network)
            .withNetworkAliases(host)
        container?.portBindings = Lists.newArrayList(String.format("%s:%s", InfluxDBContainer.PORT, InfluxDBContainer.DOCKER_PORT))
        Startables.deepStart(java.util.stream.Stream.of(container))
            .join()
        log.info("InfluxDB container started")
        given().ignoreExceptions()
            .await()
            .atMost(1, TimeUnit.MINUTES)

        injector = createInjector(
            InfluxDBModule(),
            FileManager()
        )
        configure = Configure()
        configure !!.injector = injector
        configure !!.host = container?.host
        configure !!.port = InfluxDBContainer.PORT
        configure !!.username = Optional.of(InfluxDBContainer.USERNAME)
        configure !!.password = Optional.of(InfluxDBContainer.PASSWORD)
        configure !!.env = Optional.of(mapOf("useEncryption" to false, "useHTTP2" to false, "db" to "test"))
    }

    @Test
    fun test()
    {
        injector !!.getInstance(Key.get(object : TypeLiteral<Set<Plugin>>()
        {}))
            .stream()
            .findFirst()
            .ifPresent {
                it.connect(configure)
                val response: Response = it.execute(it.validator())
                log.info("================ Plugin executed information =================")
                if (! response.isSuccessful)
                {
                    log.error("Message: {}", response.message)
                }
                else
                {
                    response.columns.forEach { column -> log.info(column.toString()) }
                }
                assertTrue(response.isSuccessful)
            }
    }
}
