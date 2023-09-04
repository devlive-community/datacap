package io.edurt.datacap.spi.executor;

public enum PipelineState
{
    CREATED,
    TIMEOUT,
    QUEUE,
    RUNNING,
    FAILURE,
    SUCCESS,
    STOPPED
}
