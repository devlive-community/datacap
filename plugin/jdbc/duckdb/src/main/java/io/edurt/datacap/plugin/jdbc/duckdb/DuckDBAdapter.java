package io.edurt.datacap.plugin.jdbc.duckdb;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class DuckDBAdapter
        extends JdbcAdapter
{
    public DuckDBAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
