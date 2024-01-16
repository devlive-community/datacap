package io.edurt.datacap.executor.seatunnel;

import io.edurt.datacap.executor.configure.ExecutorConfigure;
import io.edurt.datacap.executor.configure.ExecutorRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class SeatunnelExecutorTest
{
    private static final Set<String> supportOptions = new HashSet<String>()
    {{
        add("host");
        add("database");
        add("sql");
        add("username");
        add("password");
    }};
    private ExecutorRequest request;

    @Before
    public void before()
    {
        Properties properties = new Properties();
        properties.put("host", "127.0.0.1");
        properties.put("username", "root");
        properties.put("password", "123456");
        properties.put("database", "default");
        properties.put("sql", "SHOW DATABASES");

        ExecutorConfigure input = new ExecutorConfigure("ClickHouse", properties, supportOptions);
        ExecutorConfigure output = new ExecutorConfigure("Console");
        request = new ExecutorRequest(System.getProperty("user.dir"), input, output);
    }

    @Test
    public void start()
    {
        Assert.assertNotNull(new SeatunnelExecutor().start(request));
    }
}