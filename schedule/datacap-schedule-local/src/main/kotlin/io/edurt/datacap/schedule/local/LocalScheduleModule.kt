package io.edurt.datacap.schedule.local

import com.google.inject.multibindings.Multibinder
import io.edurt.datacap.schedule.Schedule
import io.edurt.datacap.schedule.ScheduleModule

class LocalScheduleModule : ScheduleModule() {
    override fun configure() {
        Multibinder.newSetBinder(this.binder(), Schedule::class.java)
                .addBinding()
                .to(LocalSchedule::class.java)
    }
}
