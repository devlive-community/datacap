package io.edurt.datacap.plugin.jdbc.doris

import io.edurt.datacap.spi.Plugin
import io.edurt.datacap.spi.PluginType
import io.edurt.datacap.spi.adapter.JdbcAdapter
import io.edurt.datacap.spi.connection.JdbcConfigure
import io.edurt.datacap.spi.connection.JdbcConnection
import io.edurt.datacap.spi.model.Configure
import io.edurt.datacap.spi.model.Response
import org.apache.commons.beanutils.BeanUtils
import org.apache.commons.lang3.ObjectUtils
import org.slf4j.LoggerFactory.getLogger
import java.lang.Boolean
import kotlin.Exception
import kotlin.String

class DorisPlugin : Plugin {

    private val log = getLogger(this.javaClass)

    private var jdbcConfigure: JdbcConfigure? = null
    private var connection: JdbcConnection? = null
    private var response: Response? = null

    override fun name(): String {
        return "Doris"
    }

    override fun description(): String {
        return "Integrate apache doris data sources"
    }

    override fun type(): PluginType {
        return PluginType.JDBC
    }

    override fun connect(configure: Configure?) {
        try {
            response = Response()
            jdbcConfigure = JdbcConfigure()
            BeanUtils.copyProperties(jdbcConfigure, configure)
            jdbcConfigure!!.jdbcDriver = "com.mysql.cj.jdbc.Driver"
            jdbcConfigure!!.jdbcType = "mysql"
            connection = object : JdbcConnection(jdbcConfigure, response) {}
        } catch (ex: Exception) {
            response!!.isConnected = Boolean.FALSE
            response!!.message = ex.message
        }
    }

    override fun execute(content: String?): Response {
        if (ObjectUtils.isNotEmpty(connection)) {
            log.info("Execute doris plugin logic started")
            response = connection!!.response
            val processor = JdbcAdapter(connection)
            response = processor.handlerExecute(content)
            log.info("Execute doris plugin logic end")
        }
        destroy()
        return response!!
    }

    override fun destroy() {
        if (ObjectUtils.isNotEmpty(this.connection)) {
            this.connection?.destroy();
            this.connection = null;
        }
    }
}