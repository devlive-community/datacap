package io.edurt.datacap.fs.minio

import com.google.inject.multibindings.Multibinder
import io.edurt.datacap.fs.Fs
import io.edurt.datacap.fs.FsModule

class MinIOModule : FsModule()
{
    override fun configure()
    {
        Multibinder.newSetBinder(binder(), Fs::class.java)
            .addBinding()
            .to(MinIOFs::class.java)
    }
}
