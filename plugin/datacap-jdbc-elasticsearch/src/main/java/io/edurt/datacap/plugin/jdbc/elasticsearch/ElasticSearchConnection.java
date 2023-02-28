package io.edurt.datacap.plugin.jdbc.elasticsearch;

import io.edurt.datacap.spi.connection.JdbcConfigure;
import io.edurt.datacap.spi.connection.JdbcConnection;
import io.edurt.datacap.spi.model.Response;

public class ElasticSearchConnection
        extends JdbcConnection
{
    public ElasticSearchConnection(JdbcConfigure jdbcConfigure, Response response)
    {
        super(jdbcConfigure, response);
    }
}
