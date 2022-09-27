package io.edurt.datacap.plugin.jdbc.mysql;

import io.edurt.datacap.spi.connection.JdbcConfigure;
import io.edurt.datacap.spi.connection.JdbcConnection;
import io.edurt.datacap.spi.model.Response;

public class MySQLConnection
        extends JdbcConnection
{
    public MySQLConnection(JdbcConfigure jdbcConfigure, Response response)
    {
        super(jdbcConfigure, response);
    }
}
