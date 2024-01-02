package io.edurt.datacap.parser.mysql

import io.edurt.datacap.parser.mysql.companion.ParserException
import io.edurt.datacap.parser.ParserResponse
import io.edurt.datacap.parser.type.EngineType
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object MySQLHelper {
    private val log: Logger = LoggerFactory.getLogger(MySQLHelper::class.java)

    @JvmStatic
    fun parse(sql: String): ParserResponse {
        val response = ParserResponse()
        response.engine = EngineType.MYSQL
        try {
            response.isParser = true
//            check(SqlParser().createStatement(sql, ParserOptions(ParserOptions.DecimalLiteralTreatment.AS_DECIMAL)), response)
        }
        catch (e: ParserException) {
            response.isParser = false
            response.message = e.message
            log.error("Parse error", e)
        }
        return response
    }
}
