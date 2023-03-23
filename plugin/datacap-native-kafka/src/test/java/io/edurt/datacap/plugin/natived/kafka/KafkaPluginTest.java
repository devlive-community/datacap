package io.edurt.datacap.plugin.natived.kafka;

import com.google.common.collect.Lists;
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
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.DockerLoggerFactory;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
public class KafkaPluginTest
{
    private static final String KAFKA_HOST = "kafkaCluster";
    private static final int KAFKA_PORT = 9093;
    private static final DockerImageName KAFKA_IMAGE_NAME = DockerImageName.parse("confluentinc/cp-kafka");

    private Network network;
    private KafkaContainer kafkaContainer;
    private Injector injector;
    private Configure configure;
    private Optional<Plugin> pluginOptional;

    @Before
    public void before()
    {
        network = Network.newNetwork();
        kafkaContainer = new KafkaContainer(KAFKA_IMAGE_NAME)
                .withNetwork(network)
                .withNetworkAliases(KAFKA_HOST)
                .withLogConsumer(
                        new Slf4jLogConsumer(
                                DockerLoggerFactory.getLogger(KAFKA_IMAGE_NAME.asCanonicalNameString())));
        ;
        kafkaContainer.setPortBindings(
                Lists.newArrayList(String.format("%s:%s", KAFKA_PORT, KAFKA_PORT)));
        Startables.deepStart(Stream.of(kafkaContainer)).join();
        log.info("Kafka container started");

        injector = Guice.createInjector(new KafkaPluginModule());
        configure = new Configure();
        configure.setHost(kafkaContainer.getBootstrapServers());

        Set<Plugin> plugins = injector.getInstance(Key.get(new TypeLiteral<Set<Plugin>>() {}));
        pluginOptional = plugins.stream()
                .filter(v -> v.name().equalsIgnoreCase("Kafka"))
                .findFirst();
    }

    @Test
    public void testValidator()
    {
        if (pluginOptional.isPresent()) {
            Plugin plugin = pluginOptional.get();
            plugin.connect(configure);
            Response response = plugin.execute(plugin.validator());
            plugin.destroy();
            log.info("Kafka plugin validation response {}", response);
            Assert.assertTrue(response.getIsSuccessful());
        }
    }
}
