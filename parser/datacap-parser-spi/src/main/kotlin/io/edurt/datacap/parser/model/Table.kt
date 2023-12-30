package io.edurt.datacap.parser.model

class Table {
    var database: String? = null
    var name: String? = null
    var alias: String? = null
    var columns: Set<Column> = setOf()

    override fun toString(): String = "Table(database=$database, name=$name, alias=$alias, columns=$columns)"
}
