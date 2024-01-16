package io.edurt.datacap.executor.seatunnel.connector;

import io.edurt.datacap.executor.configure.ExecutorConfigure;
import org.apache.commons.lang3.ObjectUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectorConsole
        extends Connector
{
    private static final Set<String> supportOptions = new HashSet<>();

    public ConnectorConsole(ConnectorType type, ExecutorConfigure configure)
    {
        super(type, configure, supportOptions);
    }

    @Override
    public Map<String, Object> formatToMap()
    {
        Map<String, Object> node = new ConcurrentHashMap<>();
        Properties properties = this.configure.getConfigure();
        if (ObjectUtils.isEmpty(properties)) {
            properties = new Properties();
        }
        node.put("Console", properties);
        return node;
    }
}
