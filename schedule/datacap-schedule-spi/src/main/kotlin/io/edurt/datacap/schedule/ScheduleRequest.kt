package io.edurt.datacap.schedule

data class ScheduleRequest(var name: String? = null,
                           var group: String = "DataCap-Schedule-Group",
                           var expression: String? = null,
                           var jobId: String? = null,
                           var job: ScheduleJob? = null)
