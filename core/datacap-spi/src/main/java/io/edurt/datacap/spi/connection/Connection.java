package io.edurt.datacap.spi.connection;

import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;
import io.edurt.datacap.spi.model.Time;

import java.util.Date;

public abstract class Connection
{
    private final Configure configure;
    private final Response response;
    private Object connection;

    public Connection(Configure configure, Response response)
    {
        this.configure = configure;
        this.response = response;
        Time connectionTime = new Time();
        connectionTime.setStart(new Date().getTime());
        this.connection = this.openConnection();
        connectionTime.setEnd(new Date().getTime());
        this.response.setConnection(connectionTime);
    }

    protected abstract String formatJdbcUrl();

    protected abstract java.sql.Connection openConnection();

    public Object getConnection()
    {
        return this.connection;
    }

    public Response getResponse()
    {
        return this.response;
    }

    public Configure getConfigure()
    {
        return this.configure;
    }

    public abstract void destroy();
}
