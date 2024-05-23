package io.edurt.datacap.notify

import com.google.inject.AbstractModule
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import java.util.*

class NotifyManager : AbstractModule()
{
    private val log: Logger = getLogger(NotifyManager::class.java)
    private var externalModules: Iterable<NotifyModule> = ServiceLoader.load(NotifyModule::class.java)

    override fun configure()
    {
        log.info("========== Loading notify start ==========")
        externalModules.forEach { module ->
            log.info("Installing notify [ {} ] join time [ {} ]", module.name(), System.currentTimeMillis())
            install(module)
        }
        log.info("========== Loading notify end ==========")
    }
}
