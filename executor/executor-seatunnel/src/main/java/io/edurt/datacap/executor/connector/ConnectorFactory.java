package io.edurt.datacap.executor.connector;

import com.google.common.base.Preconditions;
import io.edurt.datacap.spi.executor.PipelineField;

public class ConnectorFactory
{
    private ConnectorFactory()
    {}

    public static Connector createFormatter(ConnectorType type, PipelineField configure)
    {
        Connector instance = null;
        if (type.equals(ConnectorType.ClickHouse)) {
            instance = new ConnectorClickHouse(type, configure);
        }
        else if (type.equals(ConnectorType.Console)) {
            instance = new ConnectorConsole(type, configure);
        }
        else {
            Preconditions.checkArgument(false, "Unsupported connector type: %s", type);
        }
        return instance;
    }
}
