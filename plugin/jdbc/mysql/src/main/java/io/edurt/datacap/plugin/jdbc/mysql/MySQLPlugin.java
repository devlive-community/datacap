package io.edurt.datacap.plugin.jdbc.mysql;

import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.PluginType;
import io.edurt.datacap.spi.adapter.Adapter;
import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MySQLPlugin
        implements Plugin
{
    private MySQLConnection connection;
    private Response response;

    @Override
    public String getName()
    {
        return "MySQL";
    }

    @Override
    public PluginType getType()
    {
        return PluginType.SOURCE;
    }

    @Override
    public void connect(Configure configure)
    {
        this.response = new Response();
        this.connection = new MySQLConnection(configure, response);
        this.connection.openConnection();
    }

    @Override
    public Response execute(String content)
    {
        log.info("Execute plugin logic started");
        response = this.connection.getResponse();
        Adapter processor = new MySQLAdapter();
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
