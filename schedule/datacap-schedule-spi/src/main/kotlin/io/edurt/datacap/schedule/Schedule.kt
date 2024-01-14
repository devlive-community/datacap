package io.edurt.datacap.schedule

interface Schedule {
    fun name(): String {
        return this.javaClass
                .simpleName
                .removeSuffix("Schedule")
    }

    fun initialize(request: ScheduleRequest): ScheduleResponse

    fun start(request: ScheduleRequest): ScheduleResponse

    fun stop(request: ScheduleRequest): ScheduleResponse
}
