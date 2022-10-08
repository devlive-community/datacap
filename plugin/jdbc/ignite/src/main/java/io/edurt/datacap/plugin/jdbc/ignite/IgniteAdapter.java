package io.edurt.datacap.plugin.jdbc.ignite;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class IgniteAdapter
        extends JdbcAdapter
{
    public IgniteAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
