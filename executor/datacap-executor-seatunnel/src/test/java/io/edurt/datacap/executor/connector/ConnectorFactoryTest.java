package io.edurt.datacap.executor.connector;

import io.edurt.datacap.spi.executor.PipelineField;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertNotNull;

public class ConnectorFactoryTest
{
    private ConnectorType type = ConnectorType.ClickHouse;
    private PipelineField from;

    @Before
    public void before()
    {
        Properties properties = new Properties();
        properties.put("host", "127.0.0.1");
        properties.put("username", "root");
        properties.put("password", "123456");
        properties.put("database", "default");
        properties.put("sql", "SHOW DATABASES");
        from = PipelineField.builder()
                .configure(properties)
                .build();
    }

    @Test
    public void createFormatter()
    {
        Connector factory = ConnectorFactory.createFormatter(type, this.from);
        assertNotNull(factory.formatToMap());
    }
}