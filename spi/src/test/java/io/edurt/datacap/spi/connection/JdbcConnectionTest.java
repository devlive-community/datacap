package io.edurt.datacap.spi.connection;

import io.edurt.datacap.spi.model.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JdbcConnectionTest
{
    private JdbcConfigure jdbcConfigure;
    private Response response;

    @Before
    public void before()
    {
        this.jdbcConfigure = new JdbcConfigure();
        this.jdbcConfigure.setJdbcType("datacap");
        this.jdbcConfigure.setJdbcDriver("io.edurt.datacap.DataCapDriver");
        this.jdbcConfigure.setHost("127.0.0.1");
        this.jdbcConfigure.setPort(9096);

        this.response = new Response();
    }

    @Test
    public void testFormatJdbcUrl()
    {
        Connection connection = new JdbcConnection(this.jdbcConfigure, this.response);
        Assert.assertEquals(connection.formatJdbcUrl(), "jdbc:datacap://127.0.0.1:9096");

        this.jdbcConfigure.setSsl(Optional.ofNullable(true));
        connection = new JdbcConnection(this.jdbcConfigure, this.response);
        Assert.assertEquals(connection.formatJdbcUrl(), "jdbc:datacap://127.0.0.1:9096?ssl=true");

        Map<String, Object> env = new HashMap<>();
        env.put("useUnicode", "true");
        env.put("zeroDateTimeBehavior", "convertToNull");
        this.jdbcConfigure.setEnv(Optional.ofNullable(env));
        connection = new JdbcConnection(this.jdbcConfigure, this.response);
        Assert.assertEquals(connection.formatJdbcUrl(), "jdbc:datacap://127.0.0.1:9096?ssl=true&useUnicode=true&zeroDateTimeBehavior=convertToNull");
    }
}