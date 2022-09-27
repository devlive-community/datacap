package io.edurt.datacap.plugin.jdbc.clickhouse;

import io.edurt.datacap.spi.connection.JdbcConfigure;
import io.edurt.datacap.spi.connection.JdbcConnection;
import io.edurt.datacap.spi.model.Response;

public class ClickHouseConnection
        extends JdbcConnection
{
    public ClickHouseConnection(JdbcConfigure jdbcConfigure, Response response)
    {
        super(jdbcConfigure, response);
    }
}
