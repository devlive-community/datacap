package io.edurt.datacap.plugin.jdbc.trino;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class TrinoAdapter
        extends JdbcAdapter
{
    public TrinoAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
