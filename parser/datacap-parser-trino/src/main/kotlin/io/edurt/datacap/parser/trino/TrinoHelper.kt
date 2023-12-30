package io.edurt.datacap.parser.trino

import io.edurt.datacap.parser.ParserResponse
import io.edurt.datacap.parser.ParsingException
import io.edurt.datacap.parser.model.Column
import io.edurt.datacap.parser.node.Node
import io.edurt.datacap.parser.parser.ParserOptions
import io.edurt.datacap.parser.parser.SqlParser
import io.edurt.datacap.parser.tree.*
import io.edurt.datacap.parser.tree.query.Query
import io.edurt.datacap.parser.tree.query.QuerySpecification
import io.edurt.datacap.parser.type.EngineType
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

object TrinoHelper {
    private val log: Logger = getLogger(TrinoHelper::class.java)

    @JvmStatic
    fun parse(sql: String): ParserResponse {
        val response = ParserResponse()
        response.engine = EngineType.TRINO
        try {
            check(SqlParser().createStatement(sql, ParserOptions(ParserOptions.DecimalLiteralTreatment.AS_DECIMAL)), response)
        }
        catch (e: ParsingException) {
            response.isParser = false
            response.message = e.message
            log.error("Parse error", e)
        }
        return response
    }

    @Throws(ParsingException::class)
    private fun check(statement: Statement, response: ParserResponse) {
        if (statement is Query) {
            val query: Query = statement
            val children: List<Node> = query.getChildren()
            for (child in children) {
                eachNode(child, response)
            }
        }
    }

    @Throws(ParsingException::class)
    private fun eachNode(node: Node, response: ParserResponse) {
        when (node) {
            is Query -> {
                loopNode(node.children, response)
            }

            is QuerySpecification -> {
                loopNode(node.children, response)
            }

            is Select -> {
                node.selectItems.forEach {
                    when (it) {
                        is SingleColumn -> {
                            getColumn(it, response)
                        }
                        // If the query is all columns, add them directly to the collection
                        is AllColumns -> {
                            val column = Column()
                            column.name = it.toString()
                            response.table.columns += column
                        }
                    }
                }
            }
        }
    }

    @Throws(ParsingException::class)
    private fun loopNode(children: List<Node>, response: ParserResponse) {
        for (node in children) {
            eachNode(node, response)
        }
    }

    @Throws(ParsingException::class)
    private fun getColumn(node: Node, response: ParserResponse) {
        when (node) {
            is SingleColumn -> {
                val column = Column()
                column.name = node.toString()
                node.alias.ifPresent { column.alias = it.value }
                response.table.columns += column
            }

            else -> {
                throw ParsingException("Invalid query")
            }
        }
    }
}
