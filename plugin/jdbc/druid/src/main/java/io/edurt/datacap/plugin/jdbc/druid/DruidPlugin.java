package io.edurt.datacap.plugin.jdbc.druid;

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
public class DruidPlugin
        implements Plugin
{
    private JdbcConfigure jdbcConfigure;
    private DruidConnection connection;
    private Response response;

    @Override
    public String name()
    {
        return "Druid";
    }

    @Override
    public String description()
    {
        return "Integrate Druid data sources";
    }

    @Override
    public PluginType type()
    {
        return PluginType.SOURCE;
    }

    @Override
    public void connect(Configure configure)
    {
        try {
            this.response = new Response();
            this.jdbcConfigure = new JdbcConfigure();
            BeanUtils.copyProperties(this.jdbcConfigure, configure);
            this.jdbcConfigure.setJdbcDriver("org.apache.calcite.avatica.remote.Driver");
            this.jdbcConfigure.setJdbcType("avatica:remote:url=http");
            this.connection = new DruidConnection(this.jdbcConfigure, this.response);
        }
        catch (Exception ex) {
            this.response.setIsConnected(Boolean.FALSE);
            this.response.setMessage(ex.getMessage());
        }
    }

    @Override
    public Response execute(String content)
    {
        if (ObjectUtils.isNotEmpty(this.connection)) {
            log.info("Execute druid plugin logic started");
            this.response = this.connection.getResponse();
            JdbcAdapter processor = new DruidAdapter(this.connection);
            this.response = processor.handlerJDBCExecute(content);
            log.info("Execute druid plugin logic end");
        }
        return this.response;
    }

    @Override
    public void destroy()
    {
        if (ObjectUtils.isNotEmpty(this.connection)) {
            this.connection.destroy();
        }
    }
}
