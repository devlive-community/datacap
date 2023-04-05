package io.edurt.datacap.plugin.natived.h2;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class H2Adapter
        extends JdbcAdapter
{
    public H2Adapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
