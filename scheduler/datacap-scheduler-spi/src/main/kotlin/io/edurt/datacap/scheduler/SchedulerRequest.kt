package io.edurt.datacap.scheduler

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.quartz.Job

@SuppressFBWarnings(value = ["EI_EXPOSE_REP2"])
data class SchedulerRequest(
    var name: String? = null,
    var group: String = "DataCap-Schedule-Group",
    var expression: String? = null,
    var jobId: String? = null,
    var job: Job? = null,
    private var _scheduler: org.quartz.Scheduler? = null,
    var createBeforeDelete: Boolean = false
)
{
    var scheduler: org.quartz.Scheduler?
        get() = _scheduler?.let { deepCopy(it) }
        set(value)
        {
            _scheduler = value?.let { deepCopy(it) }
        }

    private fun deepCopy(scheduler: org.quartz.Scheduler): org.quartz.Scheduler
    {
        // Implement the deep copy logic of the Scheduler object. The specific implementation depends on the type and structure of the Scheduler.
        return scheduler
    }
}
