package io.edurt.datacap.executor

import com.google.inject.multibindings.Multibinder

class TestExecutorModule : ExecutorModule() {
    override fun configure() {
        Multibinder.newSetBinder(this.binder(), Executor::class.java)
                .addBinding()
                .to(TestExecutor::class.java)
    }
}
