package io.edurt.datacap.executor.seatunnel.connector;

import com.google.common.collect.Sets;
import io.edurt.datacap.executor.configure.ExecutorConfigure;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertNotNull;

public class ConnectorFactoryTest
{
    private ConnectorType type = ConnectorType.ClickHouse;
    private ExecutorConfigure input;

    @Before
    public void before()
    {
        Properties properties = new Properties();
        properties.put("host", "127.0.0.1");
        properties.put("username", "root");
        properties.put("password", "123456");
        properties.put("database", "default");
        properties.put("sql", "SHOW DATABASES");
        input = new ExecutorConfigure("ClickHouse", properties, Sets.newHashSet());
    }

    @Test
    public void createFormatter()
    {
        Connector factory = ConnectorFactory.createFormatter(type, this.input);
        assertNotNull(factory.formatToMap());
    }
}