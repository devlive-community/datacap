package io.edurt.datacap.executor

import com.google.inject.AbstractModule

abstract class ExecutorModule : AbstractModule() {
    open fun name(): String {
        return this.javaClass
                .simpleName
                .removeSuffix("Module")
                .removeSuffix("Executor")
    }
}
