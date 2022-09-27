package io.edurt.datacap.plugin.jdbc.clickhouse;

import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.PluginType;
import io.edurt.datacap.spi.adapter.JdbcAdapter;
import io.edurt.datacap.spi.connection.JdbcConfigure;
import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

@Slf4j
public class ClickHousePlugin
        implements Plugin
{
    private JdbcConfigure jdbcConfigure;
    private ClickHouseConnection clickHouseConnection;
    private Response response;

    @Override
    public String name()
    {
        return "ClickHouse";
    }

    @Override
    public String description()
    {
        return "Integrate ClickHouse data sources";
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
            this.jdbcConfigure.setJdbcDriver("ru.yandex.clickhouse.ClickHouseDriver");
            this.jdbcConfigure.setJdbcType("clickhouse");
            this.clickHouseConnection = new ClickHouseConnection(this.jdbcConfigure, this.response);
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
        log.info("Execute clickhouse plugin logic started");
        this.response = this.clickHouseConnection.getResponse();
        JdbcAdapter processor = new ClickHouseAdapter(this.clickHouseConnection);
        this.response = processor.handlerJDBCExecute(content);
        log.info("Execute clickhouse plugin logic end");
        return this.response;
    }

    @Override
    public void destroy()
    {
        this.clickHouseConnection.destroy();
    }
}
