package io.edurt.datacap.plugin.influxdb

import io.edurt.datacap.spi.Plugin

class InfluxDBPlugin : Plugin
{
    override fun validator(): String
    {
        return "SELECT '-' AS version"
    }

    override fun driver(): String
    {
        return "org.apache.arrow.driver.jdbc.ArrowFlightJdbcDriver"
    }

    override fun connectType(): String
    {
        return "arrow-flight-sql"
    }
}
