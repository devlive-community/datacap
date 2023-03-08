package io.edurt.datacap.executor;

import io.edurt.datacap.spi.executor.Executor;
import io.edurt.datacap.spi.executor.ExecutorConfigure;
import org.slf4j.Logger;

public class ExampleExecutor
        implements Executor
{
    @Override
    public void start(ExecutorConfigure configure)
    {
        Logger logger = (Logger) configure.getLogger().getLogger();
        logger.info("ExampleExecutor start");
    }
}
