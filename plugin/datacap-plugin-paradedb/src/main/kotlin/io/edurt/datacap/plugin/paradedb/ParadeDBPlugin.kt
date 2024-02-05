package io.edurt.datacap.plugin.paradedb

import io.edurt.datacap.spi.Plugin

class ParadeDBPlugin : Plugin {
    override fun driver(): String {
        return "org.postgresql.Driver"
    }

    override fun connectType(): String {
        return "postgresql"
    }
}
