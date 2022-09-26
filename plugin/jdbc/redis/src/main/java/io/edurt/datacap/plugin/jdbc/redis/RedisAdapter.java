package io.edurt.datacap.plugin.jdbc.redis;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class RedisAdapter
        extends JdbcAdapter
{
    public RedisAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
