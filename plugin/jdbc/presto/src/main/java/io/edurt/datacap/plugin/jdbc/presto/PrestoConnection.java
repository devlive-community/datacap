package io.edurt.datacap.plugin.jdbc.presto;

import io.edurt.datacap.spi.connection.JdbcConfigure;
import io.edurt.datacap.spi.connection.JdbcConnection;
import io.edurt.datacap.spi.model.Response;

public class PrestoConnection
        extends JdbcConnection
{
    public PrestoConnection(JdbcConfigure jdbcConfigure, Response response)
    {
        super(jdbcConfigure, response);
    }
}
