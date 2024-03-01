package io.edurt.datacap.fs.qiniu

import com.google.inject.multibindings.Multibinder
import io.edurt.datacap.fs.Fs
import io.edurt.datacap.fs.FsModule

class QiniuModule : FsModule() {
    override fun configure() {
        Multibinder.newSetBinder(binder(), Fs::class.java)
                .addBinding()
                .to(QiniuFs::class.java)
    }
}
