package io.edurt.datacap.file

import com.google.inject.AbstractModule
import io.edurt.datacap.common.utils.DateUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import java.util.*

class FileManager : AbstractModule()
{
    private val log: Logger = getLogger(FileManager::class.java)
    private var externalModules: Iterable<FileModule> = ServiceLoader.load(FileModule::class.java)

    override fun configure()
    {
        log.info("========== Loading file format start ==========")
        externalModules.forEach { module ->
            log.info("Installing file format [ {} ] join time [ {} ]", module.name(), DateUtils.formatYMDHMSWithInterval())
            install(module)
        }
        log.info("========== Loading file format end ==========")
    }
}
