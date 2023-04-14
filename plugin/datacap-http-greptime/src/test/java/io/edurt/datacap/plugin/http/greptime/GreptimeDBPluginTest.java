package io.edurt.datacap.plugin.http.greptime;

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
public class GreptimeDBPluginTest
{
    private static final String HOST = "greptimeDBCluster";
    private Network network;
    private GreptimeDBContainer container;

    private Injector injector;
    private Configure configure;

    @Before
    public void before()
    {
        network = Network.newNetwork();
        container = new GreptimeDBContainer()
                .withNetwork(network)
                .withNetworkAliases(HOST)
                .withExposedPorts(GreptimeDBContainer.HTTP_PORT)
                .withCommand("standalone", "start",
                        "--http-addr", "0.0.0.0:4000")
                .waitingFor(Wait.forHttp("/dashboard"));
        Startables.deepStart(Stream.of(container)).join();
        log.info("GreptimeDB container started");

        injector = Guice.createInjector(new GreptimeDBPluginModule());
        configure = new Configure();
        configure.setHost("localhost");
        configure.setPort(container.getMappedPort(GreptimeDBContainer.HTTP_PORT));
        configure.setDatabase(Optional.of("default"));
    }

    @Test
    public void test()
    {
        Set<Plugin> plugins = injector.getInstance(Key.get(new TypeLiteral<Set<Plugin>>() {}));
        Optional<Plugin> pluginOptional = plugins.stream()
                .filter(v -> v.name().equalsIgnoreCase("GreptimeDB"))
                .findFirst();
        if (pluginOptional.isPresent()) {
            Plugin plugin = pluginOptional.get();
            plugin.connect(configure);
            String sql = "SELECT * FROM numbers LIMIT 5";
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