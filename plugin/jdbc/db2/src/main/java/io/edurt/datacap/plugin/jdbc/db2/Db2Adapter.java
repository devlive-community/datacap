package io.edurt.datacap.plugin.jdbc.db2;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class Db2Adapter
        extends JdbcAdapter
{
    public Db2Adapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
