package io.edurt.datacap.executor.connector;

import io.edurt.datacap.spi.executor.PipelineField;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectorFactory
{
    private ConnectorFactory()
    {}

    public static Connector createFormatter(ConnectorType type, PipelineField configure)
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
                    Properties properties = new Properties();
                    this.configure.getConfigure().entrySet()
                            .stream()
                            .filter(entry -> !String.valueOf(entry.getValue()).equals("None"))
                            .forEach(entry -> properties.put(entry.getKey(), entry.getValue()));
                    node.put(this.type.name(), properties);
                    return node;
                }
            };
        }
        return instance;
    }
}
