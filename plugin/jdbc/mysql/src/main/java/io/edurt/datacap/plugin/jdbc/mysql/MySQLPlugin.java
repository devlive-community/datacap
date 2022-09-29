package io.edurt.datacap.plugin.jdbc.mysql;

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
public class MySQLPlugin
        implements Plugin
{
    private JdbcConfigure jdbcConfigure;
    private MySQLConnection mySQLConnection;
    private Response response;

    @Override
    public String name()
    {
        return "MySQL";
    }

    @Override
    public String description()
    {
        return "Integrate MySQL data sources";
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
            this.jdbcConfigure.setJdbcDriver("com.mysql.jdbc.Driver");
            this.jdbcConfigure.setJdbcType("mysql");
            this.mySQLConnection = new MySQLConnection(this.jdbcConfigure, this.response);
        }
        catch (Exception ex) {
            this.response.setIsConnected(Boolean.FALSE);
            this.response.setMessage(ex.getMessage());
        }
    }

    @Override
    public Response execute(String content)
    {
        if (ObjectUtils.isNotEmpty(this.mySQLConnection)) {
            log.info("Execute mysql plugin logic started");
            this.response = this.mySQLConnection.getResponse();
            JdbcAdapter processor = new MySQLAdapter(this.mySQLConnection);
            this.response = processor.handlerJDBCExecute(content);
            log.info("Execute mysql plugin logic end");
        }
        return this.response;
    }

    @Override
    public void destroy()
    {
        if (ObjectUtils.isNotEmpty(this.mySQLConnection)) {
            this.mySQLConnection.destroy();
        }
    }
}
