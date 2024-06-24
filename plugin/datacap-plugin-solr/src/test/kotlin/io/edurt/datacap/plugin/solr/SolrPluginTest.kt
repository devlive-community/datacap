package io.edurt.datacap.plugin.solr

import com.google.inject.Guice.createInjector
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.TypeLiteral
import io.edurt.datacap.spi.Plugin
import io.edurt.datacap.spi.model.Configure
import io.edurt.datacap.spi.model.Response
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory.getLogger
import org.testcontainers.containers.Network
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.lifecycle.Startables
import org.testcontainers.shaded.com.google.common.collect.Lists
import org.testcontainers.shaded.org.awaitility.Awaitility.given
import java.util.*
import java.util.concurrent.TimeUnit

class SolrPluginTest
{
    private val log = getLogger(this.javaClass)
    private val host = "TestSolrContainer"
    private var container: SolrContainer? = null
    private var injector: Injector? = null
    private var configure: Configure? = null

    @Before
    fun before()
    {
        val network = Network.newNetwork()
        container = SolrContainer()
            .withNetwork(network)
            .withNetworkAliases(host)
            .waitingFor(Wait.forHttp("/"))
        container?.portBindings = Lists.newArrayList(String.format("%s:%s", SolrContainer.PORT, SolrContainer.DOCKER_PORT))
        Startables.deepStart(java.util.stream.Stream.of(container)).join()
        log.info("Solr container started")
        given().ignoreExceptions()
            .await()
            .atMost(1, TimeUnit.MINUTES)

        injector = createInjector(SolrModule())
        configure = Configure()
        configure !!.host = container?.host
        configure !!.port = SolrContainer.PORT
        configure !!.database = Optional.of("test")
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
