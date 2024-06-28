package io.edurt.datacap.convert

import com.google.inject.AbstractModule

abstract class ConvertModule : AbstractModule()
{
    fun name(): String = this.javaClass
        .simpleName
        .removeSuffix("File")
        .removeSuffix("Module")
}
