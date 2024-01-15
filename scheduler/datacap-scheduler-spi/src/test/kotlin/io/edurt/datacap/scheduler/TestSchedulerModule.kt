package io.edurt.datacap.scheduler

import com.google.inject.multibindings.Multibinder

class TestSchedulerModule : SchedulerModule() {
    override fun configure() {
        Multibinder.newSetBinder(this.binder(), Scheduler::class.java)
                .addBinding()
                .to(TestScheduler::class.java)
    }
}
