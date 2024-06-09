package io.edurt.datacap.file.txt

import com.google.inject.multibindings.Multibinder
import io.edurt.datacap.file.File
import io.edurt.datacap.file.FileModule

class TxtModule : FileModule()
{
    override fun configure()
    {
        Multibinder.newSetBinder(this.binder(), File::class.java)
            .addBinding()
            .to(TxtFile::class.java)
    }
}
