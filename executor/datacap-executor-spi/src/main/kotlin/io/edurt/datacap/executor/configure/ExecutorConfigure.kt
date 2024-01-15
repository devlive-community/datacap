package io.edurt.datacap.executor.configure

import io.edurt.datacap.executor.common.Protocol
import java.util.*

data class ExecutorConfigure(var type: String,
                             var configure: Properties,
                             var supportOptions: Set<String> = mutableSetOf(),
                             var protocol: Protocol = Protocol.NONE)
