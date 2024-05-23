package io.edurt.datacap.notify

import com.google.inject.AbstractModule

abstract class NotifyModule : AbstractModule()
{
    fun name(): String = this.javaClass
        .simpleName
        .removeSuffix("Notify")
        .removeSuffix("Module")
}
