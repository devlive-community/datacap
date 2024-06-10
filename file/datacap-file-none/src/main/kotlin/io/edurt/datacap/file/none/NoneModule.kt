package io.edurt.datacap.file.none

import com.google.inject.multibindings.Multibinder
import io.edurt.datacap.file.File
import io.edurt.datacap.file.FileModule

class NoneModule : FileModule()
{
    override fun configure()
    {
        Multibinder.newSetBinder(this.binder(), File::class.java)
            .addBinding()
            .to(NoneFile::class.java)
    }
}
