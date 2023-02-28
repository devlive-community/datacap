package io.edurt.datacap.plugin.http.clickhouse;

import io.edurt.datacap.spi.connection.HttpConfigure;
import io.edurt.datacap.spi.connection.HttpConnection;
import io.edurt.datacap.spi.model.Response;

public class ClickHouseConnection
        extends HttpConnection
{
    public ClickHouseConnection(HttpConfigure httpConfigure, Response response)
    {
        super(httpConfigure, response);
    }
}
