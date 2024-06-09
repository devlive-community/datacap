package io.edurt.datacap.plugin.pinot

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import io.edurt.datacap.spi.Plugin
import io.edurt.datacap.spi.adapter.JdbcAdapter
import io.edurt.datacap.spi.connection.JdbcConfigure
import io.edurt.datacap.spi.connection.JdbcConnection
import io.edurt.datacap.spi.model.Configure
import io.edurt.datacap.spi.model.Response
import org.apache.commons.beanutils.BeanUtils
import org.apache.commons.lang3.ObjectUtils
import org.slf4j.LoggerFactory.getLogger

@SuppressFBWarnings(value = ["EI_EXPOSE_REP"])
class PinotPlugin : Plugin
{
    private val log = getLogger(PinotPlugin::class.java)

    private var jdbcConfigure: JdbcConfigure? = null
    private var jdbcConnection: JdbcConnection? = null
    private var jdbcResponse: Response? = null

    override fun validator(): String
    {
        return "select * from billing limit 10"
    }

    override fun connect(configure: Configure?)
    {
        try
        {
            log.info("Connecting to apache pinot")
            jdbcResponse = Response()
            jdbcConfigure = JdbcConfigure()
            BeanUtils.copyProperties(jdbcConfigure, configure)
            jdbcConfigure !!.jdbcDriver = "org.apache.pinot.client.PinotDriver"
            jdbcConfigure !!.jdbcType = "pinot"
            jdbcConnection = object : JdbcConnection(jdbcConfigure, jdbcResponse)
            {}
        }
        catch (ex: Exception)
        {
            jdbcResponse !!.isConnected = false
            jdbcResponse !!.message = ex.message
        }
    }

    override fun execute(content: String?): Response
    {
        if (ObjectUtils.isNotEmpty(jdbcConnection))
        {
            log.info("Apache pinot connection established")
            jdbcResponse = jdbcConnection?.response
            val processor = JdbcAdapter(jdbcConnection)
            jdbcResponse = processor.handlerExecute(content)
            log.info("Apache pinot execution completed")
        }
        destroy()
        return jdbcResponse !!
    }

    override fun destroy()
    {
        if (ObjectUtils.isNotEmpty(jdbcConnection))
        {
            log.info("Apache pinot driver destroyed")
            jdbcConnection?.destroy()
            jdbcConnection = null
        }
    }
}
