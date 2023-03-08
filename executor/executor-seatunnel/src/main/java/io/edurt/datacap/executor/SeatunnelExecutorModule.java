package io.edurt.datacap.executor;

import com.google.inject.multibindings.Multibinder;
import io.edurt.datacap.spi.executor.Executor;
import io.edurt.datacap.spi.executor.ExecutorModule;

public class SeatunnelExecutorModule
        extends ExecutorModule
{
    protected void configure()
    {
        Multibinder<Executor> plugin = Multibinder.newSetBinder(this.binder(), Executor.class);
        plugin.addBinding().to(SeatunnelExecutor.class);
    }
}
