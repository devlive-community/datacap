package io.edurt.datacap.plugin.jdbc.postgresql;

import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.PluginType;
import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostgreSQLPlugin
        implements Plugin
{
    private PostgreSQLConnection connection;
    private Response response;

    @Override
    public String name()
    {
        return "PostgreSQL";
    }

    @Override
    public String description()
    {
        return "Integrate PostgreSQL data sources";
    }

    @Override
    public PluginType type()
    {
        return PluginType.SOURCE;
    }

    @Override
    public void connect(Configure configure)
    {
        this.response = new Response();
        this.connection = new PostgreSQLConnection(configure, response);
        this.connection.openConnection();
    }

    @Override
    public Response execute(String content)
    {
        log.info("Execute plugin logic started");
        response = this.connection.getResponse();
        JdbcAdapter processor = new PostgreSQLAdapter();
        processor.handlerJDBCExecute(this.connection.getConfigure().getFormat(), content, this.connection.getConnection(), response);
        log.info("Execute plugin logic end");
        return response;
    }

    @Override
    public void destroy()
    {
        this.connection.destroy();
    }
}
