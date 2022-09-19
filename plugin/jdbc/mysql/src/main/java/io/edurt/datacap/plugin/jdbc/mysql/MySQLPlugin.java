package io.edurt.datacap.plugin.jdbc.mysql;

import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.PluginType;
import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;

public class MySQLPlugin
        implements Plugin
{
    private MySQLConnection connection;

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
        this.connection = new MySQLConnection(configure);
        this.connection.openConnection();
    }

    @Override
    public Response execute(String content)
    {
        MySQLProcessor processor = new MySQLProcessor(this.connection);
        return processor.handlerExecute(content);
    }

    @Override
    public void destroy()
    {
        this.connection.destroy();
    }
}
