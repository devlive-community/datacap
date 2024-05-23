package io.edurt.datacap.notify

import io.edurt.datacap.notify.model.NotifyRequest
import io.edurt.datacap.notify.model.NotifyResponse

class TestNotify : Notify
{
    override fun send(request: NotifyRequest): NotifyResponse
    {
        val response = NotifyResponse()
        return response
    }
}
