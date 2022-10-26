package io.edurt.datacap.plugin.jdbc.phoenix;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class PhoenixAdapter
        extends JdbcAdapter
{
    public PhoenixAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
