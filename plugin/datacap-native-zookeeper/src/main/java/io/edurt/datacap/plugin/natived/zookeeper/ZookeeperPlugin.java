package io.edurt.datacap.plugin.natived.zookeeper;

import io.edurt.datacap.spi.Plugin;
import io.edurt.datacap.spi.PluginType;
import io.edurt.datacap.spi.adapter.Adapter;
import io.edurt.datacap.spi.connection.JdbcConfigure;
import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;

@Slf4j
public class ZookeeperPlugin
        implements Plugin
{
    private Configure configure;
    private ZookeeperConnection connection;
    private Response response;

    @Override
    public String validator()
    {
        return "SHOW PATHS FROM controller";
    }

    @Override
    public String name()
    {
        return "Zookeeper";
    }

    @Override
    public String description()
    {
        return "Integrate Zookeeper data sources";
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
            this.configure = new JdbcConfigure();
            BeanUtils.copyProperties(this.configure, configure);
            this.connection = new ZookeeperConnection(this.configure, this.response);
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
            log.info("Execute zookeeper plugin logic started");
            this.response = this.connection.getResponse();
            Adapter processor = new ZookeeperAdapter(this.connection, new ZookeeperParser(content));
            this.response = processor.handlerExecute(content);
            log.info("Execute zookeeper plugin logic end");
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
