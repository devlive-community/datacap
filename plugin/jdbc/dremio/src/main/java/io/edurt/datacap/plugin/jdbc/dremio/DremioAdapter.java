package io.edurt.datacap.plugin.jdbc.dremio;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class DremioAdapter
        extends JdbcAdapter
{
    public DremioAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
