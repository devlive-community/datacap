package io.edurt.datacap.parser.mysql

import io.edurt.datacap.parser.ParserResponse
import io.edurt.datacap.parser.SqlParser

class MySQLSqlParser : SqlParser {
    override fun parse(sql: String): ParserResponse {
        return MySQLHelper.parse(sql)
    }
}
