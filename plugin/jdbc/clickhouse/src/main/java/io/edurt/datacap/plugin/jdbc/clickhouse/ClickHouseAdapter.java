package io.edurt.datacap.plugin.jdbc.clickhouse;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;

public class ClickHouseAdapter
        extends JdbcAdapter
{
    public ClickHouseAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
