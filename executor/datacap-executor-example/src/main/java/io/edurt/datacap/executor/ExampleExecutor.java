package io.edurt.datacap.executor;

import io.edurt.datacap.spi.executor.Executor;
import io.edurt.datacap.spi.executor.Pipeline;
import io.edurt.datacap.spi.executor.PipelineResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExampleExecutor
        implements Executor
{
    @Override
    public void before(Pipeline configure)
    {
        log.info("before");
    }

    @Override
    public PipelineResponse start(Pipeline configure)
    {
        return PipelineResponse.builder().build();
    }
}
