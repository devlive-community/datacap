package io.edurt.datacap.natived.hdfs

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import io.edurt.datacap.spi.adapter.NativeAdapter
import io.edurt.datacap.spi.model.Configure
import io.edurt.datacap.spi.model.Response
import io.edurt.datacap.spi.model.Time
import io.edurt.datacap.spi.parser.SqlParser
import io.edurt.datacap.sql.SqlBase
import io.edurt.datacap.sql.SqlBaseToken
import org.apache.commons.lang3.ObjectUtils
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import java.lang.Boolean
import java.util.*
import kotlin.Any
import kotlin.Exception
import kotlin.String
import kotlin.require
import kotlin.requireNotNull

@SuppressFBWarnings(value = ["BC_BAD_CAST_TO_ABSTRACT_COLLECTION", "EI_EXPOSE_REP2"])
class HdfsAdapter : NativeAdapter
{
    private val log: Logger = getLogger(HdfsAdapter::class.java)

    private var hdfsConnection: HdfsConnection? = null

    constructor(connection: HdfsConnection?, parser: SqlParser?) : super(connection, parser)
    {
        this.hdfsConnection = connection
    }

    override fun handlerExecute(content: String?): Response
    {
        val processorTime = Time()
        processorTime.start = Date().time
        val response: Response = this.connection.response
        val configure: Configure = this.connection.configure
        if (response.isConnected)
        {
            val headers: MutableList<String> = ArrayList()
            val types: MutableList<String> = ArrayList()
            val columns: MutableList<Any> = ArrayList()
            try
            {
                val sqlBase = parser.sqlBase
                if (sqlBase.isSuccessful)
                {
                    val configuration = this.hdfsConnection?.hdfsConfigure
                    val sqlBase = this.parser.sqlBase
                    if (sqlBase.isSuccessful)
                    {
                        if (ObjectUtils.isNotEmpty(parser.sqlBase.columns))
                        {
                            headers.addAll(parser.sqlBase.columns)
                        }
                        else
                        {
                            headers.add("*")
                        }
                        types.add("String")
                        this.adapter(configuration, sqlBase)
                            .forEach { column -> columns.add(handlerFormatter(configure.format, headers, Collections.singletonList(column) as List<Any>?)) }
                        response.isSuccessful = Boolean.TRUE
                    }
                    else
                    {
                        response.isSuccessful = false
                        response.message = sqlBase.message
                    }
                }
                else
                {
                    response.isSuccessful = Boolean.FALSE
                    response.message = sqlBase.message
                }
            }
            catch (ex: Exception)
            {
                log.error("Execute content failed content {} exception ", content, ex)
                response.isSuccessful = Boolean.FALSE
                response.message = ex.message
            }
            finally
            {
                response.headers = headers
                response.types = types
                response.columns = columns
            }
        }
        processorTime.end = Date().time
        response.processor = processorTime
        return response
    }

    private fun adapter(configuration: Configuration?, info: SqlBase): List<String>
    {
        requireNotNull(info.token) { "Token must not be null" }
        require(info.token.equals(SqlBaseToken.SHOW.name, ignoreCase = true)) { "Token not supported" }

        val fileSystem = FileSystem.get(configuration)

        if (info.childToken.equals("DATABASES", ignoreCase = true))
        {
            info.table = "/"
        }

        return fileSystem.listStatus(Path("/" + info.table))
            .map { it.path.name }
            .toList()
    }
}
