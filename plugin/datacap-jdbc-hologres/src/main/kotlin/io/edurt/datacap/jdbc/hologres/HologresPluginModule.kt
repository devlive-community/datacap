package io.edurt.datacap.jdbc.hologres

import com.google.inject.multibindings.Multibinder
import io.edurt.datacap.spi.AbstractPluginModule
import io.edurt.datacap.spi.Plugin
import io.edurt.datacap.spi.PluginModule
import io.edurt.datacap.spi.PluginType

class HologresPluginModule : AbstractPluginModule(), PluginModule {
    override fun getName(): String {
        return this.javaClass.simpleName.replace("PluginModule", "")
    }

    override fun getType(): PluginType {
        return PluginType.JDBC
    }

    override fun get(): AbstractPluginModule {
        return this
    }

    override fun configure() {
        val plugin: Multibinder<Plugin> = Multibinder.newSetBinder(binder(), Plugin::class.java)
        plugin.addBinding().to(HologresPlugin::class.java)
    }
}