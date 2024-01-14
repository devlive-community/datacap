package io.edurt.datacap.schedule.local

import io.edurt.datacap.schedule.Schedule
import io.edurt.datacap.schedule.ScheduleRequest
import io.edurt.datacap.schedule.ScheduleResponse

class LocalSchedule : Schedule {
    override fun initialize(request: ScheduleRequest): ScheduleResponse {
        return QuartzEndpoint.createJob(request)
    }

    override fun stop(request: ScheduleRequest): ScheduleResponse {
        return QuartzEndpoint.removeJob(request)
    }
}
