package io.edurt.datacap.schedule

import com.google.inject.AbstractModule

abstract class ScheduleModule : AbstractModule() {
    fun name(): String {
        return this.javaClass
                .simpleName
                .removeSuffix("Module")
                .removeSuffix("Schedule")
    }
}
