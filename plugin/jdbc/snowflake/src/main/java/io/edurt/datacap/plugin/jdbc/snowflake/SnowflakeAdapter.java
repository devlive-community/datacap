package io.edurt.datacap.plugin.jdbc.snowflake;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class SnowflakeAdapter
        extends JdbcAdapter
{
    public SnowflakeAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
