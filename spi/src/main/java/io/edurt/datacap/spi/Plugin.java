package io.edurt.datacap.spi;

import io.edurt.datacap.spi.model.Configure;
import io.edurt.datacap.spi.model.Response;

public interface Plugin
{
    String getName();

    PluginType getType();

    void connect(Configure configure);

    Response execute(String content);

    void destroy();
}
