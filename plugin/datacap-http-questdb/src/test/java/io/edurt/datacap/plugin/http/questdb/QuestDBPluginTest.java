package io.edurt.datacap.plugin.http.questdb;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.lifecycle.Startables;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
public class QuestDBPluginTest
{
    private static final String HOST = "questDBCluster";
    private Network network;
    private QuestDBContainer container;

    private Injector injector;
    private Configure configure;

    @Before
    public void before()
    {
        network = Network.newNetwork();
        container = new QuestDBContainer()
                .withNetwork(network)
                .withNetworkAliases(HOST)
                .withExposedPorts(QuestDBContainer.HTTP_PORT)
                .waitingFor(Wait.forHttp("/"));
        Startables.deepStart(Stream.of(container)).join();
        log.info("QuestDB container started");

        injector = Guice.createInjector(new QuestDBPluginModule());
        configure = new Configure();
        configure.setHost("localhost");
        configure.setPort(container.getMappedPort(QuestDBContainer.HTTP_PORT));
        configure.setDatabase(Optional.of("default"));
    }

    @Test
    public void test()
    {
        Set<Plugin> plugins = injector.getInstance(Key.get(new TypeLiteral<Set<Plugin>>() {}));
        Optional<Plugin> pluginOptional = plugins.stream()
                .filter(v -> v.name().equalsIgnoreCase("QuestDB"))
                .findFirst();
        if (pluginOptional.isPresent()) {
            Plugin plugin = pluginOptional.get();
            plugin.connect(configure);
            String sql = "SELECT version()";
            Response response = plugin.execute(sql);
            log.info("================ plugin executed information =================");
            if (!response.getIsSuccessful()) {
                log.error("Message: {}", response.getMessage());
            }
            else {
                response.getColumns().forEach(column -> log.info(column.toString()));
            }
            Assert.assertTrue(response.getIsSuccessful());
            plugin.destroy();
        }
    }
}