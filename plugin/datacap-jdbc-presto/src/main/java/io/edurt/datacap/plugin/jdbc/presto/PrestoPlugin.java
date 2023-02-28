package io.edurt.datacap.plugin.jdbc.presto;

import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.PluginType;
import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConfigure;
import io.edurt.datacap.spi.connection.JdbcConnection;
import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;

@Slf4j
public class PrestoPlugin
        implements Plugin
{
    private JdbcConfigure jdbcConfigure;
    private JdbcConnection jdbcConnection;
    private Response response;

    @Override
    public String validator()
    {
        return "SELECT node_version AS version\n" +
                "FROM system.runtime.nodes\n" +
                "LIMIT 1";
    }

    @Override
    public String name()
    {
        return "Presto";
    }

    @Override
    public String description()
    {
        return "Integrate Presto data sources";
    }

    @Override
    public PluginType type()
    {
        return PluginType.JDBC;
    }

    @Override
    public void connect(Configure configure)
    {
        try {
            this.response = new Response();
            this.jdbcConfigure = new JdbcConfigure();
            BeanUtils.copyProperties(this.jdbcConfigure, configure);
            this.jdbcConfigure.setJdbcDriver("io.prestosql.jdbc.PrestoDriver");
            this.jdbcConfigure.setJdbcType("presto");
            this.jdbcConnection = new JdbcConnection(this.jdbcConfigure, this.response);
        }
        catch (Exception ex) {
            this.response.setIsConnected(Boolean.FALSE);
            this.response.setMessage(ex.getMessage());
        }
    }

    @Override
    public Response execute(String content)
    {
        if (ObjectUtils.isNotEmpty(this.jdbcConnection)) {
            log.info("Execute plugin logic started");
            this.response = this.jdbcConnection.getResponse();
            JdbcAdapter processor = new PrestoAdapter(this.jdbcConnection);
            this.response = processor.handlerExecute(content);
            log.info("Execute plugin logic end");
        }
        return this.response;
    }

    @Override
    public void destroy()
    {
        if (ObjectUtils.isNotEmpty(this.jdbcConnection)) {
            this.jdbcConnection.destroy();
        }
    }
}
