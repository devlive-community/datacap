package io.edurt.datacap.scylladb

import com.datastax.oss.driver.api.core.cql.ResultSet
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import io.edurt.datacap.spi.adapter.Adapter
import io.edurt.datacap.spi.model.Configure
import io.edurt.datacap.spi.model.Response
import io.edurt.datacap.spi.model.Time
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import java.lang.Boolean
import java.util.*
import kotlin.Any
import kotlin.Exception
import kotlin.String
import kotlin.toString

@SuppressFBWarnings(
        value = ["RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE"], justification = "I prefer to suppress these FindBugs warnings"
)
class ScyllaDBAdapter : Adapter {
    private val log: Logger = getLogger(this.javaClass)

    private var connection: ScyllaDBConnection? = null

    constructor(connection: ScyllaDBConnection?) : super() {
        this.connection = connection
    }

    override fun handlerExecute(content: String?): Response {
        val processorTime = Time()
        processorTime.start = Date().time
        val response: Response = this.connection !!.response
        val configure: Configure = this.connection !!.configure
        if (response.isConnected) {
            val headers: MutableList<String> = ArrayList()
            val types: MutableList<String> = ArrayList()
            val columns: MutableList<Any> = ArrayList()
            try {
                val resultSet: ResultSet = connection?.getSession() !!.execute(content !!)
                var isPresent = true
                resultSet.forEach { row ->
                    if (isPresent) {
                        row.columnDefinitions.forEach {
                            types.add(it.type.asCql(true, true))
                            headers.add(it.name.asCql(true))
                        }
                        isPresent = false
                    }
                    val _columns: MutableList<Any> = ArrayList()
                    headers.forEach {
                        _columns.add(row.getObject(it).toString())
                    }
                    columns.add(handlerFormatter(configure.format, headers, _columns))
                }
                response.isSuccessful = Boolean.TRUE
            }
            catch (ex: Exception) {
                log.error("Execute content failed content {} exception ", content, ex)
                response.isSuccessful = Boolean.FALSE
                response.message = ex.message
            }
            finally {
                response.headers = headers
                response.types = types
                response.columns = columns
            }
        }
        processorTime.end = Date().time
        response.processor = processorTime
        return response
    }
}
