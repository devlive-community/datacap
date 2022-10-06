package io.edurt.datacap.plugin.jdbc.kylin;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class KylinAdapter
        extends JdbcAdapter
{
    public KylinAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
