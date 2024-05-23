package io.edurt.datacap.notify

import io.edurt.datacap.notify.model.NotifyRequest
import io.edurt.datacap.notify.model.NotifyResponse

interface Notify
{
    fun name(): String
    {
        return this.javaClass
            .simpleName
            .removeSuffix("Notify")
    }

    fun description(): String
    {
        return "Integrate ${name()} notify"
    }

    fun send(request: NotifyRequest): NotifyResponse
}
