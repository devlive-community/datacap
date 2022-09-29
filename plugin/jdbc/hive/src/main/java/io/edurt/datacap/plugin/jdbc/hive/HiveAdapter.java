package io.edurt.datacap.plugin.jdbc.hive;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class HiveAdapter
        extends JdbcAdapter
{
    public HiveAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
