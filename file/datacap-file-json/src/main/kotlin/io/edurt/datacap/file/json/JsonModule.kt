package io.edurt.datacap.file.json

import com.google.inject.multibindings.Multibinder
import io.edurt.datacap.file.File
import io.edurt.datacap.file.FileModule

class JsonModule : FileModule()
{
    override fun configure()
    {
        Multibinder.newSetBinder(this.binder(), File::class.java)
            .addBinding()
            .to(JsonFile::class.java)
    }
}
