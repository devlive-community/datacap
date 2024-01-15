package io.edurt.datacap.scheduler

interface Scheduler {
    fun name(): String {
        return this.javaClass
                .simpleName
                .removeSuffix("Scheduler")
    }

    fun initialize(request: SchedulerRequest): SchedulerResponse

    fun stop(request: SchedulerRequest): SchedulerResponse
}
