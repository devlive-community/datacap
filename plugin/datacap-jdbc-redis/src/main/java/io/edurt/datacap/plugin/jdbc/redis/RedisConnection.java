package io.edurt.datacap.plugin.jdbc.redis;

import io.edurt.datacap.spi.connection.JdbcConfigure;
import io.edurt.datacap.spi.connection.JdbcConnection;
import io.edurt.datacap.spi.model.Response;

public class RedisConnection
        extends JdbcConnection
{
    public RedisConnection(JdbcConfigure jdbcConfigure, Response response)
    {
        super(jdbcConfigure, response);
    }
}
