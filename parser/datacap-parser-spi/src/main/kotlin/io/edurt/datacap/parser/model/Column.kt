package io.edurt.datacap.parser.model

class Column {
    var name: String? = null
    var type: String? = null
    var alias: String? = null
    var expression: String? = null
    var functions: Set<String>? = null

    override fun toString(): String = "Column(name=$name, type=$type, alias=$alias, expression=$expression, functions=$functions)"
}
