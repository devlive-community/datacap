package io.edurt.datacap.plugin.http.clickhouse;

import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.PluginType;
import io.edurt.datacap.spi.adapter.Adapter;
import io.edurt.datacap.spi.connection.HttpConfigure;
import io.edurt.datacap.spi.connection.HttpConnection;
import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;

@Slf4j
public class ClickHousePlugin
        implements Plugin
{
    private HttpConfigure httpConfigure;
    private HttpConnection connection;
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
        return PluginType.HTTP;
    }

    @Override
    public void connect(Configure configure)
    {
        try {
            this.response = new Response();
            this.httpConfigure = new HttpConfigure();
            BeanUtils.copyProperties(this.httpConfigure, configure);
            this.connection = new ClickHouseConnection(this.httpConfigure, this.response);
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
            log.info("Execute clickhouse plugin logic started");
            this.response = this.connection.getResponse();
            Adapter processor = new ClickHouseAdapter(this.connection);
            this.response = processor.handlerExecute(content);
            log.info("Execute clickhouse plugin logic end");
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
