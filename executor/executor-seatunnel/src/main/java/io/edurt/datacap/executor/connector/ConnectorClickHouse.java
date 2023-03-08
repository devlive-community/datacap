package io.edurt.datacap.executor.connector;

import io.edurt.datacap.spi.executor.PipelineField;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectorClickHouse
        extends Connector
{
    private static final String[] supportOptions = {
            "host",
            "database",
            "sql",
            "username",
            "password"
    };

    public ConnectorClickHouse(ConnectorType type, PipelineField configure)
    {
        super(type, configure, supportOptions);
    }

    @Override
    public Map<String, Object> formatToMap()
    {
        Map<String, Object> node = new ConcurrentHashMap<>();
        node.put("Clickhouse", this.configure.getConfigure());
        return node;
    }
}
