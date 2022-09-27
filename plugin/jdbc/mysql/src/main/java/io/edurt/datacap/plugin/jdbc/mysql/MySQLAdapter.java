package io.edurt.datacap.plugin.jdbc.mysql;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class MySQLAdapter
        extends JdbcAdapter
{
    public MySQLAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
