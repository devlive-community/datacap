package io.edurt.datacap.plugin.jdbc.sqlserver;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class SqlServerAdapter
        extends JdbcAdapter
{
    public SqlServerAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
