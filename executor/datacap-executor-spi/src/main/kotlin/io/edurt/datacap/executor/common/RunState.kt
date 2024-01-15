package io.edurt.datacap.executor.common

enum class RunState {
    CREATED,
    TIMEOUT,
    QUEUE,
    RUNNING,
    FAILURE,
    SUCCESS,
    STOPPED
}
