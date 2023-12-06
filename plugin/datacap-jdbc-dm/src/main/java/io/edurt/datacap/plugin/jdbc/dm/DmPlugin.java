package io.edurt.datacap.plugin.jdbc.dm;

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
public class DmPlugin
        implements Plugin
{
    private JdbcConfigure jdbcConfigure;
    private DmConnection connection;
    private Response response;

    @Override
    public String name()
    {
        return "Dameng";
    }

    @Override
    public String description()
    {
        return "Integrate Dameng data sources";
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
            this.jdbcConfigure.setJdbcDriver("dm.jdbc.driver.DmDriver");
            this.jdbcConfigure.setJdbcType("dm");
            this.connection = new DmConnection(this.jdbcConfigure, this.response);
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
            log.info("Execute dameng plugin logic started");
            this.response = this.connection.getResponse();
            JdbcAdapter processor = new DmAdapter(this.connection);
            this.response = processor.handlerExecute(content);
            log.info("Execute dameng plugin logic end");
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
