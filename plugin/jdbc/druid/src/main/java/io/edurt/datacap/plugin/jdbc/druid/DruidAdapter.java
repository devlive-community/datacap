package io.edurt.datacap.plugin.jdbc.druid;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class DruidAdapter
        extends JdbcAdapter
{
    public DruidAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
