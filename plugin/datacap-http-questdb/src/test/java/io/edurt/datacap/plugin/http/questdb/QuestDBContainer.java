package io.edurt.datacap.plugin.http.questdb;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class QuestDBContainer
        extends GenericContainer<QuestDBContainer>
{
    private static final DockerImageName DEFAULT_IMAGE_NAME =
            DockerImageName.parse("questdb/questdb");

    public static final int HTTP_PORT = 9000;
    public static final int INFLUXDB_PORT = 9009;
    public static final int POSTGRES_PORT = 8812;
    public static final int PORT = 9003;

    public QuestDBContainer()
    {
        super(DEFAULT_IMAGE_NAME);
        withExposedPorts(PORT, HTTP_PORT, INFLUXDB_PORT, POSTGRES_PORT);
    }

    public QuestDBContainer(final DockerImageName dockerImageName)
    {
        super(dockerImageName);
        dockerImageName.assertCompatibleWith(dockerImageName);
        withExposedPorts(PORT, HTTP_PORT, INFLUXDB_PORT, POSTGRES_PORT);
    }
}
