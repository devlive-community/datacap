package io.edurt.datacap.jdbc.hologres

import io.edurt.datacap.spi.Plugin
import io.edurt.datacap.spi.adapter.JdbcAdapter
import io.edurt.datacap.spi.connection.JdbcConfigure
import io.edurt.datacap.spi.connection.JdbcConnection
import io.edurt.datacap.spi.model.Configure
import io.edurt.datacap.spi.model.Response
import org.apache.commons.beanutils.BeanUtils
import org.apache.commons.lang3.ObjectUtils
import org.slf4j.LoggerFactory.getLogger

class HologresPlugin : Plugin {
    private val log = getLogger(HologresPlugin::class.java)

    private var jdbcConfigure: JdbcConfigure? = null
    private var jdbcConnection: JdbcConnection? = null
    private var jdbcResponse: Response? = null
    override fun name(): String {
        return this.javaClass.simpleName.replace("Plugin", "")
    }

    override fun description(): String {
        return String.format("Integrate %s data sources", this.name())
    }

    override fun connect(configure: Configure?) {
        try {
            log.info("Connecting to Hologres")
            jdbcResponse = Response()
            jdbcConfigure = JdbcConfigure()
            BeanUtils.copyProperties(jdbcConfigure, configure)
            jdbcConfigure!!.jdbcDriver = "org.postgresql.Driver"
            jdbcConfigure!!.jdbcType = "postgresql"
            jdbcConnection = object : JdbcConnection(jdbcConfigure, jdbcResponse) {}
        } catch (ex: Exception) {
            jdbcResponse!!.isConnected = false
            jdbcResponse!!.message = ex.message
        }
    }

    override fun execute(content: String?): Response {
        if (ObjectUtils.isNotEmpty(jdbcConnection)) {
            log.info("Execute hologres plugin logic started")
            jdbcResponse = jdbcConnection?.response
            val processor = JdbcAdapter(jdbcConnection)
            jdbcResponse = processor.handlerExecute(content)
            log.info("Execute hologres plugin logic end")
        }
        destroy()
        return jdbcResponse!!
    }

    override fun destroy() {
        if (ObjectUtils.isNotEmpty(jdbcConnection)) {
            jdbcConnection?.destroy()
            jdbcConnection = null
        }
    }
}