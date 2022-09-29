package io.edurt.datacap.plugin.jdbc.kyuubi;

import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.PluginType;
import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConfigure;
import io.edurt.datacap.spi.connection.JdbcConnection;
import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

@Slf4j
public class KyuubiPlugin
        implements Plugin
{
    private JdbcConfigure jdbcConfigure;
    private JdbcConnection connection;
    private Response response;

    @Override
    public String name()
    {
        return "Kyuubi";
    }

    @Override
    public String description()
    {
        return "Integrate Kyuubi data sources";
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
            this.jdbcConfigure.setJdbcDriver("org.apache.kyuubi.jdbc.KyuubiHiveDriver");
            this.jdbcConfigure.setJdbcType("hive2");
            this.connection = new JdbcConnection(this.jdbcConfigure, this.response);
        }
        catch (Exception ex) {
            this.response.setIsSuccessful(Boolean.FALSE);
            this.response.setMessage(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Response execute(String content)
    {
        log.info("Execute kyuubi plugin logic started");
        this.response = this.connection.getResponse();
        JdbcAdapter processor = new KyuubiAdapter(this.connection);
        this.response = processor.handlerJDBCExecute(content);
        log.info("Execute kyuubi plugin logic end");
        return this.response;
    }

    @Override
    public void destroy()
    {
        this.connection.destroy();
    }
}
