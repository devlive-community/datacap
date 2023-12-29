package io.edurt.datacap.parser

import io.edurt.datacap.parser.model.Table
import io.edurt.datacap.parser.type.EngineType
import io.edurt.datacap.parser.type.StatementType

class ParserResponse {
    var table: Table? = null
    var type: StatementType = StatementType.SELECT
    var engine: EngineType = EngineType.MYSQL

    override fun toString(): String = "ParserResponse(table=$table, type=$type, engine=$engine)"
}
