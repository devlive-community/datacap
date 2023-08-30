package io.edurt.datacap.executor;

import io.edurt.datacap.spi.executor.Executor;
import io.edurt.datacap.spi.executor.Pipeline;
import io.edurt.datacap.spi.executor.PipelineResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

@Slf4j
public class ExampleExecutor
        implements Executor
{
    @Override
    public String before(Pipeline configure, Logger logger)
    {
        log.info("before");
        return null;
    }

    @Override
    public PipelineResponse start(Pipeline configure)
    {
        return PipelineResponse.builder().build();
    }
}
