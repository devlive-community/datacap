package io.edurt.datacap.executor.connector;

import com.google.common.base.Preconditions;
import io.edurt.datacap.spi.executor.PipelineField;

import java.util.Map;

public abstract class Connector
{
    protected final ConnectorType type;
    protected final PipelineField configure;
    private final String[] supportOptions;

    public Connector(ConnectorType type, PipelineField configure, String[] supportOptions)
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

    public abstract Map<String, Object> formatToMap();
}
