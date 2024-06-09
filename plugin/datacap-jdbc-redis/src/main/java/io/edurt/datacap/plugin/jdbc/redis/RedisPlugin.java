package io.edurt.datacap.plugin.jdbc.redis;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
@SuppressFBWarnings(value = {"EI_EXPOSE_REP"})
public class RedisPlugin
        implements Plugin
{
    private JdbcConfigure jdbcConfigure;
    private JdbcConnection jdbcConnection;
    private Response response;

    @Override
    public String validator()
    {
        return "set app datacap";
    }

    @Override
    public String name()
    {
        return "Redis";
    }

    @Override
    public String description()
    {
        return "Integrate Redis data sources";
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
            this.jdbcConfigure.setJdbcDriver("io.edurt.datacap.driver.redis.RedisDriver");
            this.jdbcConfigure.setJdbcType("redis");
            this.jdbcConfigure.setUsername(configure.getUsername().map(u -> "".equals(u) ? null : u));
            this.jdbcConfigure.setPassword(configure.getPassword().map(pwd -> "".equals(pwd) ? null : pwd));
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
            JdbcAdapter processor = new RedisAdapter(this.jdbcConnection);
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
