package io.edurt.datacap.plugin.jdbc.cratedb;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class CrateDBAdapter
        extends JdbcAdapter
{
    public CrateDBAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
