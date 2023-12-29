package io.edurt.datacap.parser.model

class Column {
    var name: String? = null
    var type: String? = null
    var alias: String? = null
    var expression: String? = null
    var functions: Set<String>? = null
}
