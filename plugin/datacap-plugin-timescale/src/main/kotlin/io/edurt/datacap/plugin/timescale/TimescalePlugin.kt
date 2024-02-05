package io.edurt.datacap.plugin.timescale

import io.edurt.datacap.spi.Plugin

class TimescalePlugin : Plugin {
    override fun driver(): String {
        return "org.postgresql.Driver"
    }

    override fun connectType(): String {
        return "postgresql"
    }
}
