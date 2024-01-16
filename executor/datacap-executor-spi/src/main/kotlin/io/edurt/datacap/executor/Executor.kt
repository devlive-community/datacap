package io.edurt.datacap.executor

import io.edurt.datacap.executor.configure.ExecutorRequest
import io.edurt.datacap.executor.configure.ExecutorResponse

interface Executor {
    fun name(): String {
        return this.javaClass
                .simpleName
                .removeSuffix("Executor")
    }

    fun start(request: ExecutorRequest): ExecutorResponse

    fun stop(request: ExecutorRequest): ExecutorResponse
}
