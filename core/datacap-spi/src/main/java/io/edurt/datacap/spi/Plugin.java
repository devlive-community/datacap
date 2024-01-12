package io.edurt.datacap.spi;

import io.edurt.datacap.spi.adapter.Adapter;
import io.edurt.datacap.spi.adapter.HttpAdapter;
import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.adapter.NativeAdapter;
import io.edurt.datacap.spi.connection.Connection;
import io.edurt.datacap.spi.connection.JdbcConfigure;
import io.edurt.datacap.spi.connection.JdbcConnection;
import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Plugin
{
    ThreadLocal<Connection> local = new ThreadLocal<>();
    Logger log = LoggerFactory.getLogger(Plugin.class);

    default String validator()
    {
        return "SELECT version() AS version";
    }

    default PluginType type()
    {
        return PluginType.JDBC;
    }

    default String name()
    {
        return this.getClass().getSimpleName().replace("Plugin", "");
    }

    default String description()
    {
        return String.format("Integrate %s data sources", this.name());
    }

    default String driver()
    {
        return "io.edurt.datacap.JdbcDriver";
    }

    default String connectType()
    {
        return "datacap";
    }

    default void connect(Configure configure)
    {
        Response response = new Response();
        try {
            JdbcConfigure jdbcConfigure = new JdbcConfigure();
            if (jdbcConfigure.getJdbcDriver() == null) {
                jdbcConfigure.setJdbcDriver(this.driver());
            }
            if (jdbcConfigure.getJdbcType() == null) {
                jdbcConfigure.setJdbcType(this.connectType());
            }
            BeanUtils.copyProperties(jdbcConfigure, configure);
            local.set(new JdbcConnection(jdbcConfigure, response));
        }
        catch (Exception ex) {
            response.setIsConnected(Boolean.FALSE);
            response.setMessage(ex.getMessage());
            log.error("Error connecting : {}", ex);
        }
    }

    default Response execute(String content)
    {
        Connection connection = local.get();
        Response response = new Response();
        if (connection != null) {
            log.info("Execute [ {} ] plugin started", this.name());
            Adapter adapter;
            if (type().equals(PluginType.JDBC)) {
                adapter = new JdbcAdapter(connection);
            }
            else if (type().equals(PluginType.NATIVE)) {
                adapter = new NativeAdapter(connection);
            }
            else {
                adapter = new HttpAdapter(connection);
            }
            response = adapter.handlerExecute(content);
            log.info("Execute [ {} ] plugin end", this.name());
        }
        return response;
    }

    default void destroy()
    {
        Connection connection = local.get();
        if (connection != null) {
            connection.destroy();
            local.remove();
        }
    }
}
