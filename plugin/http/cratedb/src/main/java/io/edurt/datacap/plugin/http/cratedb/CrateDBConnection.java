package io.edurt.datacap.plugin.http.cratedb;

import io.edurt.datacap.spi.connection.HttpConfigure;
import io.edurt.datacap.spi.connection.HttpConnection;
import io.edurt.datacap.spi.model.Response;

public class CrateDBConnection
        extends HttpConnection
{
    public CrateDBConnection(HttpConfigure httpConfigure, Response response)
    {
        super(httpConfigure, response);
    }
}
