package io.edurt.datacap.plugin.jdbc.doris

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

class DorisPluginTest {
    private val log = getLogger(this.javaClass)

    private val host = "dorisCluster"
    private var container: DorisContainer? = null

    private var injector: Injector? = null
    private var configure: Configure? = null

    @Before
    fun before() {
        val network = Network.newNetwork()
        container = DorisContainer()
                ?.withNetwork(network)
                ?.withNetworkAliases(host)
                ?.withPrivilegedMode(true)
        container?.portBindings = Lists.newArrayList(String.format("%s:%s", DorisContainer.PORT, DorisContainer.DOCKER_PORT));
        Startables.deepStart(java.util.stream.Stream.of(container)).join()
        log.info("Apache Doris container started")
        given().ignoreExceptions()
                .await()
                .atMost(10000, TimeUnit.SECONDS)

        injector = Guice.createInjector(DorisPluginModule())
        configure = Configure()
        configure!!.host = container?.host ?: host
        configure!!.port = DorisContainer.PORT
        configure!!.username = Optional.of("root")
        configure!!.password = Optional.of("")
    }

    @Test
    fun test() {
        val plugins: Set<Plugin?>? = injector?.getInstance(Key.get(object : TypeLiteral<Set<Plugin?>?>() {}))
        val plugin: Plugin? = plugins?.first { v -> v?.name().equals("Doris") }
        if (ObjectUtils.isNotEmpty(plugin)) {
            plugin?.connect(configure)
            val sql = "SELECT version()"
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
