package io.edurt.datacap.plugin.natived.h2;

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
public class H2Plugin
        implements Plugin
{
    private JdbcConfigure jdbcConfigure;
    private JdbcConnection connection;
    private Response response;

    @Override
    public String validator()
    {
        return "SELECT SETTING_VALUE AS version\n" +
                "FROM INFORMATION_SCHEMA.SETTINGS\n" +
                "WHERE SETTING_NAME  = 'info.VERSION'";
    }

    @Override
    public String name()
    {
        return "H2";
    }

    @Override
    public String description()
    {
        return "Integrate H2 mem data sources";
    }

    @Override
    public PluginType type()
    {
        return PluginType.NATIVE;
    }

    @Override
    public void connect(Configure configure)
    {
        try {
            this.response = new Response();
            this.jdbcConfigure = new JdbcConfigure();
            BeanUtils.copyProperties(this.jdbcConfigure, configure);
            this.jdbcConfigure.setJdbcDriver("org.h2.Driver");
            this.jdbcConfigure.setJdbcType("h2:mem");
            this.connection = new H2Connection(this.jdbcConfigure, this.response);
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
            log.info("Execute h2 plugin logic started");
            this.response = this.connection.getResponse();
            JdbcAdapter processor = new H2Adapter(this.connection);
            this.response = processor.handlerExecute(content);
            log.info("Execute h2 plugin logic end");
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
