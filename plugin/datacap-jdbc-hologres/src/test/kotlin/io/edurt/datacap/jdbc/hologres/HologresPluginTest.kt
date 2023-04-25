package io.edurt.datacap.jdbc.hologres

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
import org.testcontainers.lifecycle.Startables
import org.testcontainers.shaded.com.google.common.collect.Lists
import org.testcontainers.shaded.org.awaitility.Awaitility.given
import java.util.*
import java.util.Set
import java.util.concurrent.TimeUnit

class HologresPluginTest {
    private val log = getLogger(HologresPluginTest::class.java)

    private val host = "HologresCluster"
    private val username = "root"
    private val password = "12345678"
    private var container: HologresContainer? = null
    private var injector: Injector? = null
    private var configure: Configure? = null

    @Before
    fun before() {
        val network = Network.newNetwork()
        container = HologresContainer()
                ?.withNetwork(network)
                ?.withNetworkAliases(host)
                ?.withEnv("POSTGRES_USER", username)
                ?.withEnv("POSTGRES_PASSWORD", password)
        container?.portBindings = Lists.newArrayList(String.format("%s:%s", HologresContainer.RPC_PORT, HologresContainer.RPC_PORT))
        Startables.deepStart(java.util.stream.Stream.of(container)).join()
        log.info("Hologres container started")

        given().ignoreExceptions()
                .await()
                .atMost(400, TimeUnit.SECONDS)

        injector = Guice.createInjector(HologresPluginModule())
        configure = Configure()
        configure!!.host = "localhost"
        configure!!.port = HologresContainer.RPC_PORT
        configure!!.username = Optional.of(username)
        configure!!.password = Optional.of(password)
        configure!!.database = Optional.of("default")
    }

    @Test
    fun test() {
        val plugin: Plugin? = injector?.getInstance(Key.get(object : TypeLiteral<Set<Plugin?>?>() {}))
                ?.first { v -> v?.name().equals("Hologres") }
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