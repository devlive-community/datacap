package io.edurt.datacap.executor.configure

import io.edurt.datacap.executor.common.State

data class ExecutorResponse(var state: State,
                            var timeout: Boolean = false,
                            var successful: Boolean = false,
                            var message: String? = null)
