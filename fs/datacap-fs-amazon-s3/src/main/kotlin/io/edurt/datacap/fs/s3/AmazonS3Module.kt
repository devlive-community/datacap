package io.edurt.datacap.fs.s3

import com.google.inject.multibindings.Multibinder
import io.edurt.datacap.fs.Fs
import io.edurt.datacap.fs.FsModule

class AmazonS3Module : FsModule()
{
    override fun configure()
    {
        Multibinder.newSetBinder(binder(), Fs::class.java)
            .addBinding()
            .to(AmazonS3Fs::class.java)
    }
}
