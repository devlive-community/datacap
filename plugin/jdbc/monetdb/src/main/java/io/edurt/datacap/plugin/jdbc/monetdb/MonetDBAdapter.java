package io.edurt.datacap.plugin.jdbc.monetdb;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class MonetDBAdapter
        extends JdbcAdapter
{
    public MonetDBAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
