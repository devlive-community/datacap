package io.edurt.datacap.natived.hdfs

import com.google.inject.multibindings.Multibinder
import io.edurt.datacap.spi.AbstractPluginModule
import io.edurt.datacap.spi.Plugin
import io.edurt.datacap.spi.PluginModule
import io.edurt.datacap.spi.PluginType

class HdfsModule : AbstractPluginModule(), PluginModule {
    override fun getType(): PluginType {
        return PluginType.NATIVE
    }

    override fun get(): AbstractPluginModule {
        return this
    }

    override fun configure() {
        Multibinder.newSetBinder(binder(), Plugin::class.java)
            .addBinding()
            .to(HdfsPlugin::class.java)
    }
}
