package io.edurt.datacap.executor.local

import com.google.inject.multibindings.Multibinder
import io.edurt.datacap.executor.Executor
import io.edurt.datacap.executor.ExecutorModule

class LocalExecutorModule : ExecutorModule() {
    override fun name(): String {
        return "Default"
    }

    override fun configure() {
        Multibinder.newSetBinder(this.binder(), Executor::class.java)
                .addBinding()
                .to(LocalExecutor::class.java)
    }
}
