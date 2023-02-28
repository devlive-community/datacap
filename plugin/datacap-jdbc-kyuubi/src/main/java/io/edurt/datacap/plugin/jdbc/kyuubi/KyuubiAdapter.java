package io.edurt.datacap.plugin.jdbc.kyuubi;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class KyuubiAdapter
        extends JdbcAdapter
{
    public KyuubiAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
