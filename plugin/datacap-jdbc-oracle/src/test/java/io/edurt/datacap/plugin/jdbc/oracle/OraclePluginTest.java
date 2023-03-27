package io.edurt.datacap.plugin.jdbc.oracle;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.model.Configure;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.DockerLoggerFactory;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
public class OraclePluginTest
{
    private static final DockerImageName ORACLE_IMAGE_NAME = DockerImageName.parse("gvenzl/oracle-xe:21-slim-faststart");
    private static final String ORACLE_HOST = "OracleContainer";
    private static final int ORACLE_PORT = 1522;
    private static final String ORACLE_USER = "Oracle";
    private static final String ORACLE_PASSWORD = "12345678";
    private static final String ORACLE_DATABASE = "TestDatabase";

    private Injector injector;
    private Configure configure;
    private Network network;
    private OracleContainer oracleContainer;

    @Before
    public void before()
    {
        network = Network.newNetwork();
        oracleContainer = new OracleContainer(ORACLE_IMAGE_NAME)
                .withNetwork(network)
                .withNetworkAliases(ORACLE_HOST)
                .withDatabaseName(ORACLE_DATABASE)
                .withUsername(ORACLE_USER)
                .withPassword(ORACLE_PASSWORD)
                .withLogConsumer(new Slf4jLogConsumer(DockerLoggerFactory.getLogger(ORACLE_IMAGE_NAME.asCanonicalNameString())));
        oracleContainer.setPortBindings(Lists.newArrayList(String.format("%s:%s", ORACLE_PORT, ORACLE_PORT)));
        Startables.deepStart(Stream.of(oracleContainer)).join();
        log.info("Oracle container started");

        injector = Guice.createInjector(new OraclePluginModule());
        configure = new Configure();
        configure.setHost(oracleContainer.getHost());
        configure.setPort(oracleContainer.getOraclePort());
        configure.setUsername(Optional.of(ORACLE_USER));
        configure.setPassword(Optional.of(ORACLE_PASSWORD));
    }

    @Test
    public void test()
    {
        Set<Plugin> plugins = injector.getInstance(Key.get(new TypeLiteral<Set<Plugin>>() {}));
        Optional<Plugin> pluginOptional = plugins.stream()
                .filter(v -> v.name().equalsIgnoreCase("Oracle"))
                .findFirst();
        if (pluginOptional.isPresent()) {
            Plugin plugin = pluginOptional.get();
            plugin.connect(configure);
            Assert.assertNotNull(plugin.execute(plugin.validator()).getConnection());
            plugin.destroy();
        }
    }
}