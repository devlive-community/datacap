package io.edurt.datacap.scheduler.local

import com.google.inject.multibindings.Multibinder
import io.edurt.datacap.scheduler.Scheduler
import io.edurt.datacap.scheduler.SchedulerModule

class LocalSchedulerModule : SchedulerModule() {
    override fun name(): String {
        return "Default"
    }

    override fun configure() {
        Multibinder.newSetBinder(this.binder(), Scheduler::class.java)
                .addBinding()
                .to(LocalScheduler::class.java)
    }
}
