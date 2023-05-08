package io.edurt.datacap.plugin.mongo

import io.edurt.datacap.spi.Plugin
import io.edurt.datacap.spi.adapter.JdbcAdapter
import io.edurt.datacap.spi.connection.JdbcConfigure
import io.edurt.datacap.spi.connection.JdbcConnection
import io.edurt.datacap.spi.model.Configure
import io.edurt.datacap.spi.model.Response
import org.apache.commons.beanutils.BeanUtils
import org.apache.commons.lang3.ObjectUtils.isNotEmpty
import org.slf4j.LoggerFactory.getLogger

class MongoPlugin : Plugin {

    private val log = getLogger(MongoPlugin::class.java)

    private var jdbcConfigure: JdbcConfigure? = null
    private var jdbcConnection: JdbcConnection? = null
    private var jdbcResponse: Response? = null

    override fun name(): String {
        return "MongoDB Community"
    }

    override fun validator(): String {
        return "select * from system.version"
    }

    override fun connect(configure: Configure?) {
        try {
            log.info("Connecting to mongodb community")
            jdbcResponse = Response()
            jdbcConfigure = JdbcConfigure()
            BeanUtils.copyProperties(jdbcConfigure, configure)
            jdbcConfigure!!.jdbcDriver = "mongodb.jdbc.MongoDriver"
            jdbcConfigure!!.jdbcType = "mongo"
            jdbcConnection = object : JdbcConnection(jdbcConfigure, jdbcResponse) {}
        } catch (ex: Exception) {
            jdbcResponse!!.isConnected = false
            jdbcResponse!!.message = ex.message
        }
    }

    override fun execute(content: String?): Response {
        if (isNotEmpty(jdbcConnection)) {
            log.info("Mongodb community connection established")
            jdbcResponse = jdbcConnection?.response
            val processor = JdbcAdapter(jdbcConnection)
            jdbcResponse = processor.handlerExecute(content)
            log.info("Mongodb community execution completed")
        }
        destroy()
        return jdbcResponse!!
    }

    override fun destroy() {
        if (isNotEmpty(jdbcConnection)) {
            log.info("Mongodb community driver destroyed")
            jdbcConnection?.destroy()
            jdbcConnection = null
        }
    }
}