package io.edurt.datacap.plugin.influxdb

import com.google.inject.multibindings.Multibinder
import io.edurt.datacap.spi.AbstractPluginModule
import io.edurt.datacap.spi.Plugin
import io.edurt.datacap.spi.PluginModule

class InfluxDBModule : AbstractPluginModule(), PluginModule
{
    override fun get(): AbstractPluginModule
    {
        return this
    }

    override fun configure()
    {
        Multibinder.newSetBinder(binder(), Plugin::class.java)
            .addBinding()
            .to(InfluxDBPlugin::class.java)
    }
}
