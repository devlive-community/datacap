package io.edurt.datacap.scheduler

import org.quartz.Job

data class SchedulerRequest(var name: String? = null,
                            var group: String = "DataCap-Schedule-Group",
                            var expression: String? = null,
                            var jobId: String? = null,
                            var job: Job? = null,
                            var scheduler: org.quartz.Scheduler? = null)
