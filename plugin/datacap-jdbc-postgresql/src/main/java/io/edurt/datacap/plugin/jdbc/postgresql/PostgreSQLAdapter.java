package io.edurt.datacap.plugin.jdbc.postgresql;

import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConnection;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostgreSQLAdapter
        extends JdbcAdapter
{
    public PostgreSQLAdapter(JdbcConnection jdbcConnection)
    {
        super(jdbcConnection);
    }
}
