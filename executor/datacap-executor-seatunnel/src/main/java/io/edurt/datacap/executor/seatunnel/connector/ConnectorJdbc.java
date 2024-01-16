package io.edurt.datacap.executor.seatunnel.connector;

import com.google.common.collect.Maps;
import io.edurt.datacap.executor.configure.ExecutorConfigure;

import java.util.Map;

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
        node.put("Jdbc", formatToProperties(configure.getConfigure()));
        return node;
    }
}
