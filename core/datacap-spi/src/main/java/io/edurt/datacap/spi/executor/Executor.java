package io.edurt.datacap.spi.executor;

import org.slf4j.Logger;

public interface Executor
{
    default String name()
    {
        // Intercept the suffix Executor to mark the executor name
        return this.getClass().getSimpleName()
                .replace("Executor", "")
                .replace("Module", "");
    }

    default String description()
    {
        return String.format("Integrate %s executor", this.name());
    }

    /**
     * A description of the before function.
     *
     * @param configure The Pipeline object used for configuration.
     * @param logger The Logger object used for logging.
     * @return The result of the before function.
     */
    String before(Pipeline configure, Logger logger);

    PipelineResponse start(Pipeline configure);
}
