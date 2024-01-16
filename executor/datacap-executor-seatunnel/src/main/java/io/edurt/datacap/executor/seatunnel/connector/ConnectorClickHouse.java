package io.edurt.datacap.executor.seatunnel.connector;

import io.edurt.datacap.executor.configure.ExecutorConfigure;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectorClickHouse
        extends Connector
{
    public ConnectorClickHouse(ConnectorType type, ExecutorConfigure configure)
    {
        super(type, configure, configure.getSupportOptions());
    }

    @Override
    public Map<String, Object> formatToMap()
    {
        Map<String, Object> node = new ConcurrentHashMap<>();
        node.put("Clickhouse", formatToProperties(configure.getConfigure()));
        return node;
    }
}
