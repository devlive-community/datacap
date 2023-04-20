package io.edurt.datacap.plugin.jdbc.starrocks

import com.google.inject.multibindings.Multibinder
import io.edurt.datacap.spi.AbstractPluginModule
import io.edurt.datacap.spi.Plugin
import io.edurt.datacap.spi.PluginModule
import io.edurt.datacap.spi.PluginType

class StarRocksPluginModule : AbstractPluginModule(), PluginModule {
    override fun getName(): String {
        return "StarRocks"
    }

    override fun getType(): PluginType {
        return PluginType.JDBC
    }

    override fun get(): AbstractPluginModule {
        return this
    }

    override fun configure() {
        val module = Multibinder.newSetBinder(binder(), String::class.java)
        module.addBinding().toInstance(this.javaClass.simpleName)
        val plugin: Multibinder<Plugin> = Multibinder.newSetBinder(binder(), Plugin::class.java)
        plugin.addBinding().to(StarRocksPlugin::class.java)
    }
}