package io.edurt.datacap.parser.model

class Table {
    var database: String? = null
    var name: String? = null
    var alias: String? = null
    var columns: MutableSet<Column> = mutableSetOf()
    var limit: Long? = null

    override fun toString(): String = "Table(database=$database, name=$name, alias=$alias, columns=$columns, limit=$limit)"
}
