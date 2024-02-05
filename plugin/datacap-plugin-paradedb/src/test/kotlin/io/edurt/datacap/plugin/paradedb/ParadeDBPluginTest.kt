package io.edurt.datacap.plugin.paradedb

import com.google.common.collect.Lists
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.TypeLiteral
import io.edurt.datacap.spi.Plugin
import io.edurt.datacap.spi.model.Configure
import io.edurt.datacap.spi.model.Response
import org.apache.commons.lang3.ObjectUtils.isNotEmpty
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

class ParadeDBPluginTest {
    private val log = getLogger(this.javaClass)
    private val host = "TestParadeDBContainer"
    private val name = "ParadeDB"
    private var container: ParadeDBContainer? = null
    private var injector: Injector? = null
    private var configure: Configure? = null

    @Before
    fun before() {
        val network = Network.newNetwork()
        container = ParadeDBContainer()
                .withNetwork(network)
                .withNetworkAliases(host)
                .waitingFor(Wait.forListeningPort())
        container?.portBindings = Lists.newArrayList(String.format("%s:%s", ParadeDBContainer.PORT, ParadeDBContainer.DOCKER_PORT))
        Startables.deepStart(java.util.stream.Stream.of(container)).join()
        log.info("ParadeDB container started")
        given().ignoreExceptions()
                .await()
                .atMost(1, TimeUnit.MINUTES)

        injector = Guice.createInjector(ParadeDBModule())
        configure = Configure()
        configure !!.host = container?.host
        configure !!.port = ParadeDBContainer.PORT
        configure !!.username = Optional.of(ParadeDBContainer.USERNAME)
        configure !!.password = Optional.of(ParadeDBContainer.PASSWORD)
        configure !!.database = Optional.of(ParadeDBContainer.DATABASE)
    }

    @Test
    fun test() {
        val plugins: Set<Plugin?>? = injector?.getInstance(Key.get(object : TypeLiteral<Set<Plugin?>?>() {}))
        val plugin: Plugin? = plugins?.first { v -> v?.name().equals(name) }
        if (isNotEmpty(plugin)) {
            plugin?.connect(configure)
            val response: Response = plugin !!.execute(plugin.validator())
            log.info("================ Plugin executed information =================")
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
