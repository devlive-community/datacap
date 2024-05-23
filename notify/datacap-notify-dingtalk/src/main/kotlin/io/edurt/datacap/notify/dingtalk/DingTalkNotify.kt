package io.edurt.datacap.notify.dingtalk

import io.edurt.datacap.notify.Notify
import io.edurt.datacap.notify.model.NotifyRequest
import io.edurt.datacap.notify.model.NotifyResponse

class DingTalkNotify : Notify
{
    override fun send(request: NotifyRequest): NotifyResponse
    {
        return DingTalkUtils.send(request)
    }
}
