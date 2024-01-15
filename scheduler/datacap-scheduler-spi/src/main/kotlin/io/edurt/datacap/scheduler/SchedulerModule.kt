package io.edurt.datacap.scheduler

import com.google.inject.AbstractModule

abstract class SchedulerModule : AbstractModule() {
    open fun name(): String {
        return this.javaClass
                .simpleName
                .removeSuffix("Module")
                .removeSuffix("Scheduler")
    }
}
