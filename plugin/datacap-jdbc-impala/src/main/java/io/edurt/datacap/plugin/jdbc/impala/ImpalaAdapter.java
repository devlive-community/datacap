package io.edurt.datacap.plugin.jdbc.impala;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class ImpalaAdapter
        extends JdbcAdapter
{
    public ImpalaAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
