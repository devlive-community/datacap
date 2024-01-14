package io.edurt.datacap.schedule.local

import io.edurt.datacap.schedule.ScheduleRequest
import io.edurt.datacap.schedule.ScheduleResponse
import org.quartz.*
import org.quartz.impl.StdSchedulerFactory
import org.slf4j.LoggerFactory


object QuartzEndpoint {
    private val log = LoggerFactory.getLogger(this.javaClass)
    private val scheduler: Scheduler = StdSchedulerFactory.getDefaultScheduler()

    @JvmStatic
    fun createJob(request: ScheduleRequest): ScheduleResponse {
        val response = ScheduleResponse()
        val name = getJobName(request)
        val group = getJobGroup(request)
        try {
            val jobDetail: JobDetail = request.job?.let {
                JobBuilder.newJob(it::class.java)
                        .withIdentity(name, group)
                        .build()
            } ?: throw IllegalArgumentException("Job is null")

            val trigger: Trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger-${request.name}", "trigger-group-${request.group}")
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(request.expression))
                    .build()

            log.info("Add new job [ {} ] to group [ {} ]", name, group)
            scheduler.scheduleJob(jobDetail, trigger)

            if (! scheduler.isStarted) {
                log.info("Scheduler starting")
                scheduler.start()
            }
            response.successful = true
            log.info("Add new job [ {} ] to group [ {} ] successful", name, group)
        }
        catch (ex: Exception) {
            log.info("Add new job [ {} ] to group [ {} ] failure ", name, group, ex)
            response.successful = false
            response.message = ex.message
        }
        return response
    }

    @JvmStatic
    fun removeJob(request: ScheduleRequest): ScheduleResponse {
        val response = ScheduleResponse()
        val name = getJobName(request)
        val group = getJobGroup(request)
        try {
            log.info("Remove job [ {} ] from group [ {} ]", name, group)
            scheduler.deleteJob(JobKey(name, group))
            response.successful = true
            log.info("Remove job [ {} ] from group [ {} ] successful", name, group)
        }
        catch (ex: Exception) {
            log.info("Remove job [ {} ] from group [ {} ] failure ", name, group, ex)
            response.successful = false
            response.message = ex.message
        }
        return response
    }

    private fun getJobName(request: ScheduleRequest): String {
        return "job-${request.name}"
    }

    private fun getJobGroup(request: ScheduleRequest): String {
        return "job-group-${request.group}"
    }
}
