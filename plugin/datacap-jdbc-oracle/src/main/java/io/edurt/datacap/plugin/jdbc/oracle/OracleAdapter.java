package io.edurt.datacap.plugin.jdbc.oracle;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class OracleAdapter
        extends JdbcAdapter
{
    public OracleAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
