package io.edurt.datacap.fs.alioss

import com.google.inject.multibindings.Multibinder
import io.edurt.datacap.fs.Fs
import io.edurt.datacap.fs.FsModule

class AliOssModule : FsModule()
{
    override fun configure()
    {
        Multibinder.newSetBinder(binder(), Fs::class.java)
            .addBinding()
            .to(AliOssFs::class.java)
    }
}
