package io.edurt.datacap.spi.executor;

import com.google.inject.AbstractModule;

public class ExecutorModule
        extends AbstractModule
{
    String name()
    {
        // Intercept the suffix Executor to mark the executor name
        return this.getClass().getSimpleName()
                .replace("Executor", "")
                .replace("Module", "");
    }
}
