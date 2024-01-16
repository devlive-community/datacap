package io.edurt.datacap.executor.seatunnel.connector;

import io.edurt.datacap.executor.configure.ExecutorConfigure;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectorFactory
{
    private ConnectorFactory()
    {}

    public static Connector createFormatter(ConnectorType type, ExecutorConfigure configure)
    {
        Connector instance;
        if (type.equals(ConnectorType.ClickHouse)) {
            instance = new ConnectorClickHouse(type, configure);
        }
        else if (type.equals(ConnectorType.Console)) {
            instance = new ConnectorConsole(type, configure);
        }
        else if (type.equals(ConnectorType.Jdbc)) {
            instance = new ConnectorJdbc(type, configure);
        }
        else {
            instance = new Connector(type, configure, configure.getSupportOptions())
            {
                @Override
                public Map<String, Object> formatToMap()
                {
                    Map<String, Object> node = new ConcurrentHashMap<>();
                    node.put(this.type.name(), formatToProperties(configure.getConfigure()));
                    return node;
                }
            };
        }
        return instance;
    }
}
