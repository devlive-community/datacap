package io.edurt.datacap.plugin.jdbc.postgresql;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.PluginType;
import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConfigure;
import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;

@Slf4j
@SuppressFBWarnings(value = {"EI_EXPOSE_REP"})
public class PostgreSQLPlugin
        implements Plugin
{
    private JdbcConfigure jdbcConfigure;
    private PostgreSQLConnection postgreSQLConnection;
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
        return PluginType.JDBC;
    }

    @Override
    public void connect(Configure configure)
    {
        try {
            this.response = new Response();
            this.jdbcConfigure = new JdbcConfigure();
            BeanUtils.copyProperties(this.jdbcConfigure, configure);
            this.jdbcConfigure.setJdbcDriver("org.postgresql.Driver");
            this.jdbcConfigure.setJdbcType("postgresql");
            this.postgreSQLConnection = new PostgreSQLConnection(this.jdbcConfigure, this.response);
        }
        catch (Exception ex) {
            this.response.setIsConnected(Boolean.FALSE);
            this.response.setMessage(ex.getMessage());
        }
    }

    @Override
    public Response execute(String content)
    {
        if (ObjectUtils.isNotEmpty(this.postgreSQLConnection)) {
            log.info("Execute postgresql plugin logic started");
            this.response = this.postgreSQLConnection.getResponse();
            JdbcAdapter processor = new PostgreSQLAdapter(this.postgreSQLConnection);
            this.response = processor.handlerExecute(content);
            log.info("Execute postgresql plugin logic end");
        }
        return this.response;
    }

    @Override
    public void destroy()
    {
        if (ObjectUtils.isNotEmpty(this.postgreSQLConnection)) {
            this.postgreSQLConnection.destroy();
        }
    }
}
