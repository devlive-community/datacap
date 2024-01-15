package io.edurt.datacap.executor.common

enum class State {
    CREATED,
    TIMEOUT,
    QUEUE,
    RUNNING,
    FAILURE,
    SUCCESS,
    STOPPED
}
