package io.edurt.datacap.parser

import io.edurt.datacap.parser.model.Table

class TestSqlParser : SqlParser
{
    override fun parse(sql: String): ParserResponse
    {
        val response = ParserResponse()
        val table = Table()
        table.name = "Test"
        response.table = table
        return response
    }
}
