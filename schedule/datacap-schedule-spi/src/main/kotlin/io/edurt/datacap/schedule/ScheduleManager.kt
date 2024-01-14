package io.edurt.datacap.schedule

import com.google.inject.AbstractModule
import org.slf4j.LoggerFactory
import java.util.*


class ScheduleManager : AbstractModule {
    private val log = LoggerFactory.getLogger(this.javaClass)
    private var externalModules: Iterable<ScheduleModule>? = null

    constructor() {
        this.externalModules = ServiceLoader.load(ScheduleModule::class.java)
    }

    override fun configure() {
        log.info("================ Schedule started ================")
        externalModules !!.forEach { module ->
            log.info("Install schedule [ {} ]", module.name())
            this.install(module)
        }
        log.info("================ Schedule end ================")
    }
}
