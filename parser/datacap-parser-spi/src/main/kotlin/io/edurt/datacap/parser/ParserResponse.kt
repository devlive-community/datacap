package io.edurt.datacap.parser

import io.edurt.datacap.parser.model.Table
import io.edurt.datacap.parser.type.EngineType
import io.edurt.datacap.parser.type.StatementType

class ParserResponse
{
    private var _table: Table = Table()
    var type: StatementType = StatementType.UNKNOWN
    var engine: EngineType = EngineType.MYSQL
    var isParser: Boolean = false
    var message: String? = null

    var table: Table
        get() = _table.copy()
        set(value)
        {
            _table = value.copy()
        }

    override fun toString(): String = "ParserResponse(table=$table, type=$type, engine=$engine)"
}
