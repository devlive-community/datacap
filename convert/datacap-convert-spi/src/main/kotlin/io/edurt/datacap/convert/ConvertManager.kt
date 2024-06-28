package io.edurt.datacap.convert

import com.google.inject.AbstractModule
import io.edurt.datacap.common.utils.DateUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import java.util.*

class ConvertManager : AbstractModule()
{
    private val log: Logger = getLogger(ConvertManager::class.java)
    private var externalModules: Iterable<ConvertModule> = ServiceLoader.load(ConvertModule::class.java)

    override fun configure()
    {
        log.info("========== Loading convert start ==========")
        externalModules.forEach { module ->
            log.info("Installing convert [ {} ] join time [ {} ]", module.name(), DateUtils.formatYMDHMSWithInterval())
            install(module)
        }
        log.info("========== Loading convert end ==========")
    }
}
