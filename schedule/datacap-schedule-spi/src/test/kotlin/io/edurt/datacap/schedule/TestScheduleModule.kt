package io.edurt.datacap.schedule

import com.google.inject.multibindings.Multibinder

class TestScheduleModule : ScheduleModule() {
    override fun configure() {
        Multibinder.newSetBinder(this.binder(), Schedule::class.java)
                .addBinding()
                .to(TestSchedule::class.java)
    }
}
