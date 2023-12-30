package io.edurt.datacap.parser.trino

import io.edurt.datacap.parser.SqlParser
import io.edurt.datacap.parser.ParserResponse

class TrinoSqlParser : SqlParser {
    override fun parse(sql: String): ParserResponse {
        return TrinoHelper.parse(sql)
    }
}
