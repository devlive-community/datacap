package io.edurt.datacap.executor.connector;

import com.google.common.base.Preconditions;
import io.edurt.datacap.spi.executor.PipelineField;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Connector
{
    protected final ConnectorType type;
    protected final PipelineField configure;
    private final Set<String> supportOptions;

    public Connector(ConnectorType type, PipelineField configure, Set<String> supportOptions)
    {
        this.type = type;
        this.configure = configure;
        this.supportOptions = supportOptions;
        this.checkSupportOptions();
    }

    private void checkSupportOptions()
    {
        for (String option : supportOptions) {
            if (!configure.getConfigure().keySet().contains(option)) {
                Preconditions.checkArgument(false, String.format("Type %s option %s is not supported", type, option));
            }
        }
    }

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
}
