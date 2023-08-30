package io.edurt.datacap.executor.connector;

import io.edurt.datacap.spi.executor.PipelineField;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectorClickHouse
        extends Connector
{
    public ConnectorClickHouse(ConnectorType type, PipelineField configure)
    {
        super(type, configure, configure.getSupportOptions());
    }

    @Override
    public Map<String, Object> formatToMap()
    {
        Map<String, Object> node = new ConcurrentHashMap<>();
        Properties properties = new Properties();
        this.configure.getConfigure().entrySet()
                .stream()
                .filter(entry -> !String.valueOf(entry.getValue()).equals("None"))
                .forEach(entry -> properties.put(entry.getKey(), entry.getValue()));
        node.put("Clickhouse", properties);
        return node;
    }
}
