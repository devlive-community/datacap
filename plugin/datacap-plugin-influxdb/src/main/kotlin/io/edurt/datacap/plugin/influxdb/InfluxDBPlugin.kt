package io.edurt.datacap.plugin.influxdb

import io.edurt.datacap.spi.Plugin

class InfluxDBPlugin : Plugin
{
    override fun validator(): String
    {
        return "SELECT 1"
    }

    override fun driver(): String
    {
        return "net.suteren.jdbc.influxdb.InfluxDbDriver"
    }

    override fun connectType(): String
    {
        return "influxdb"
    }
}
