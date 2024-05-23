package io.edurt.datacap.notify.dingtalk

import com.google.inject.multibindings.Multibinder
import io.edurt.datacap.notify.Notify
import io.edurt.datacap.notify.NotifyModule

class DingTalkModule : NotifyModule()
{
    override fun configure()
    {
        Multibinder.newSetBinder(this.binder(), Notify::class.java)
            .addBinding()
            .to(DingTalkNotify::class.java)
    }
}
