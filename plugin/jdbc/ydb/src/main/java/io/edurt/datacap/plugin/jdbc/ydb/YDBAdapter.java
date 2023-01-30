package io.edurt.datacap.plugin.jdbc.ydb;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class YDBAdapter
        extends JdbcAdapter
{
    public YDBAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
