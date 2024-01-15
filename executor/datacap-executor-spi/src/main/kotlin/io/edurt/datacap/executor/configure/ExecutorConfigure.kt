package io.edurt.datacap.executor.configure

import io.edurt.datacap.executor.common.RubProtocol
import io.edurt.datacap.spi.Plugin
import io.edurt.datacap.spi.model.Configure
import java.util.*

data class ExecutorConfigure(var type: String,
                             var configure: Properties,
                             var supportOptions: Set<String> = mutableSetOf(),
                             var protocol: RubProtocol = RubProtocol.NONE,
                             var plugin: Plugin? = null,
                             var query: String? = null,
                             var database: String? = null,
                             var table: String? = null,
                             var originConfigure: Configure? = null,
                             var originColumns: Set<OriginColumn> = mutableSetOf())
