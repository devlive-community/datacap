package io.edurt.datacap.plugin.natived.ceresdb;

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
import org.testcontainers.lifecycle.Startables;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
public class CeresDBPluginTest
{
    private static final String HOST = "ceresDBCluster";
    private Network network;
    private CeresDBContainer container;

    private Injector injector;
    private Configure configure;

    @Before
    public void before()
    {
        network = Network.newNetwork();
        container = new CeresDBContainer().withNetwork(network).withNetworkAliases(HOST).withExposedPorts(CeresDBContainer.HTTP_PORT);
        Startables.deepStart(Stream.of(container)).join();
        log.info("CeresDB container started");

        injector = Guice.createInjector(new CeresDBPluginModule());
        configure = new Configure();
        configure.setHost("localhost");
        configure.setPort(container.getMappedPort(CeresDBContainer.HTTP_PORT));
        configure.setDatabase(Optional.of("default"));
    }

    @Test
    public void test()
    {
        Set<Plugin> plugins = injector.getInstance(Key.get(new TypeLiteral<Set<Plugin>>() {}));
        Optional<Plugin> pluginOptional = plugins.stream().filter(v -> v.name().equalsIgnoreCase("CeresDB")).findFirst();
        if (pluginOptional.isPresent()) {
            Plugin plugin = pluginOptional.get();
            plugin.connect(configure);
            String sql = "SELECT * FROM system.public.tables";
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