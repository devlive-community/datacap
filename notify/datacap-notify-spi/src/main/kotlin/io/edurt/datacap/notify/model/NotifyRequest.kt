package io.edurt.datacap.notify.model

import io.edurt.datacap.notify.NotifyType

class NotifyRequest
{
    var title: String? = null
    var content: String? = null
    var sendTo: String? = null
    var access: String? = null
    var secret: String? = null
    var type: NotifyType = NotifyType.TEXT
}
