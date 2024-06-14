package io.edurt.datacap.file.csv

import com.google.inject.multibindings.Multibinder
import io.edurt.datacap.file.File
import io.edurt.datacap.file.FileModule

class CsvModule : FileModule()
{
    override fun configure()
    {
        Multibinder.newSetBinder(this.binder(), File::class.java)
            .addBinding()
            .to(CsvFile::class.java)
    }
}
