package io.edurt.datacap.plugin.jdbc.oceanbase;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class OceanBaseAdapter
        extends JdbcAdapter
{
    public OceanBaseAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
