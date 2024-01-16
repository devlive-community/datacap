package io.edurt.datacap.executor

import com.google.inject.AbstractModule
import org.slf4j.LoggerFactory.getLogger
import java.time.LocalDateTime
import java.util.*

class ExecutorManager : AbstractModule {
    private val log = getLogger(this.javaClass)
    private var externalModules: Iterable<ExecutorModule>? = null

    constructor() {
        this.externalModules = ServiceLoader.load(ExecutorModule::class.java)
    }

    override fun configure() {
        log.info("================ Executor started ================")
        externalModules !!.forEach { module ->
            log.info("Install Executor [ {} ] Join time [ {} ]", module.name(), LocalDateTime.now())
            this.install(module)
        }
        log.info("================ Executor end ================")
    }
}
