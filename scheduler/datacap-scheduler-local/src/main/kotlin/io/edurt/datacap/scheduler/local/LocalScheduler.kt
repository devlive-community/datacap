package io.edurt.datacap.scheduler.local

import io.edurt.datacap.scheduler.Scheduler
import io.edurt.datacap.scheduler.SchedulerRequest
import io.edurt.datacap.scheduler.SchedulerResponse

class LocalScheduler : Scheduler {
    override fun name(): String {
        return "Default"
    }

    override fun initialize(request: SchedulerRequest): SchedulerResponse {
        return QuartzEndpoint.createJob(request)
    }

    override fun stop(request: SchedulerRequest): SchedulerResponse {
        return QuartzEndpoint.removeJob(request)
    }
}
