package io.edurt.datacap.file

import com.google.inject.AbstractModule

abstract class FileModule : AbstractModule()
{
    fun name(): String = this.javaClass
        .simpleName
        .removeSuffix("File")
        .removeSuffix("Module")
}
