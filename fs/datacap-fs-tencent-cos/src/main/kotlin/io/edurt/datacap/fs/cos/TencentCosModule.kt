package io.edurt.datacap.fs.cos

import com.google.inject.multibindings.Multibinder
import io.edurt.datacap.fs.Fs
import io.edurt.datacap.fs.FsModule

class TencentCosModule : FsModule()
{
    override fun configure()
    {
        Multibinder.newSetBinder(binder(), Fs::class.java)
            .addBinding()
            .to(TencentCosFs::class.java)
    }
}
