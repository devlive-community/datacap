package io.edurt.datacap.plugin.jdbc.presto;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class PrestoAdapter
        extends JdbcAdapter
{
    public PrestoAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
