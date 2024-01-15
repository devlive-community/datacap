package io.edurt.datacap.scheduler

import com.google.inject.AbstractModule
import org.slf4j.LoggerFactory
import java.time.LocalTime
import java.util.*


class ScheduleManager : AbstractModule {
    private val log = LoggerFactory.getLogger(this.javaClass)
    private var externalModules: Iterable<SchedulerModule>? = null

    constructor() {
        this.externalModules = ServiceLoader.load(SchedulerModule::class.java)
    }

    override fun configure() {
        log.info("================ Scheduler started ================")
        externalModules !!.forEach { module ->
            log.info("Install scheduler [ {} ] Join time [ {} ]", module.name(), LocalTime.now())
            this.install(module)
        }
        log.info("================ Scheduler end ================")
    }
}
