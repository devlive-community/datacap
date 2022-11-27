package io.edurt.datacap.plugin.jdbc.tdengine;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class TDengineAdapter
        extends JdbcAdapter
{
    public TDengineAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
