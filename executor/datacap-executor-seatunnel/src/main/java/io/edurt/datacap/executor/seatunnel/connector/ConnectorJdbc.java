package io.edurt.datacap.executor.seatunnel.connector;

import com.google.common.collect.Maps;
import io.edurt.datacap.executor.configure.ExecutorConfigure;

import java.util.Map;
import java.util.Properties;

public class ConnectorJdbc
        extends Connector
{
    public ConnectorJdbc(ConnectorType type, ExecutorConfigure configure)
    {
        super(type, configure, configure.getSupportOptions());
    }

    @Override
    public Map<String, Object> formatToMap()
    {
        Map<String, Object> node = Maps.newConcurrentMap();
        Properties properties = new Properties();
        this.configure.getConfigure().entrySet()
                .stream()
                .filter(entry -> !String.valueOf(entry.getValue()).equals("None"))
                .forEach(entry -> properties.put(entry.getKey(), entry.getValue()));
        node.put("Jdbc", properties);
        return node;
    }
}
