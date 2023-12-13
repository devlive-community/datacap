package io.edurt.datacap.plugin.sqlserver

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.TypeLiteral
import io.edurt.datacap.plugin.jdbc.sqlserver.SqlServerPluginModule
import io.edurt.datacap.plugin.jdbc.sqlserver.SqlServerVersion
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
import org.testcontainers.utility.DockerImageName
import java.util.*
import java.util.concurrent.TimeUnit

class SqlServer2017Test {
    private val log = getLogger(this.javaClass)

    private val host = "TestSqlServer2017Container"
    private var container: SqlServerContainer? = null

    private var injector: Injector? = null
    private var configure: Configure? = null

    @Before
    fun before() {
        val network = Network.newNetwork()
        container = SqlServerContainer(DockerImageName.parse(SqlServerContainer.IMAGE_2017_LATEST))
                ?.withNetwork(network)
                ?.withNetworkAliases(host)
        container?.portBindings = Lists.newArrayList(String.format("%s:%s", SqlServerContainer.PORT, SqlServerContainer.DOCKER_PORT));
        Startables.deepStart(java.util.stream.Stream.of(container)).join()
        log.info("SQL Server 2017 container started")
        given().ignoreExceptions()
                .await()
                .atMost(10000, TimeUnit.SECONDS)

        injector = Guice.createInjector(SqlServerPluginModule())
        configure = Configure()
        configure!!.host = container?.host ?: host
        configure!!.port = SqlServerContainer.PORT
        configure!!.username = Optional.of("sa")
        configure!!.password = Optional.of(SqlServerContainer.PASSWORD)
        configure!!.ssl = Optional.of(true)
    }

    @Test
    fun test() {
        configure!!.version = Optional.of(SqlServerVersion.V2017.version)
        val plugins: Set<Plugin?>? = injector?.getInstance(Key.get(object : TypeLiteral<Set<Plugin?>?>() {}))
        val plugin: Plugin? = plugins?.first { v -> v?.name().equals("SqlServer") }
        if (ObjectUtils.isNotEmpty(plugin)) {
            plugin?.connect(configure)
            val response: Response = plugin!!.execute(plugin.validator())
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
