package io.edurt.datacap.scylladb

import com.google.common.collect.Lists
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
import org.testcontainers.containers.Network
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.lifecycle.Startables
import org.testcontainers.shaded.org.awaitility.Awaitility.given
import java.util.*
import java.util.concurrent.TimeUnit

class ScyllaDBPluginTest {
    private val log = getLogger(this.javaClass)
    private val host = "TestScyllaDBContainer"
    private val name = "ScyllaDB"

    private var container: ScyllaDBContainer? = null
    private var injector: Injector? = null
    private var configure: Configure? = null

    @Before
    fun before() {
        val network = Network.newNetwork()
        container = ScyllaDBContainer()
                .withNetwork(network)
                .withNetworkAliases(host)
                .waitingFor(Wait.forListeningPort())
        container?.portBindings = Lists.newArrayList(String.format("%s:%s", ScyllaDBContainer.PORT, ScyllaDBContainer.DOCKER_PORT))
        Startables.deepStart(java.util.stream.Stream.of(container)).join()
        log.info("ScyllaDB container started")
        given().ignoreExceptions()
                .await()
                .atMost(1, TimeUnit.MINUTES)

        injector = Guice.createInjector(ScyllaDBModule())
        configure = Configure()
        configure !!.host = container?.host ?: host
        configure !!.port = ScyllaDBContainer.PORT
        configure !!.database = Optional.of("datacenter1")
    }

    @Test
    fun test() {
        val plugins: Set<Plugin?>? = injector?.getInstance(Key.get(object : TypeLiteral<Set<Plugin?>?>() {}))
        val plugin: Plugin? = plugins?.first { v -> v?.name().equals(name) }
        if (ObjectUtils.isNotEmpty(plugin)) {
            plugin?.connect(configure)
            val response: Response = plugin !!.execute(plugin.validator())
            log.info("================ plugin executed information =================")
            if (! response.isSuccessful) {
                log.error("Message: {}", response.message)
            }
            else {
                response.columns.forEach { column -> log.info(column.toString()) }
            }
            Assert.assertTrue(response.isSuccessful)
        }
    }
}
