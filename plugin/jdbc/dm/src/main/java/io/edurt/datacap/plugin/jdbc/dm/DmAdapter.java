package io.edurt.datacap.plugin.jdbc.dm;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class DmAdapter
        extends JdbcAdapter
{
    public DmAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
