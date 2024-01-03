package io.edurt.datacap.parser.mysql

import com.google.inject.multibindings.Multibinder
import io.edurt.datacap.parser.ParserModule
import io.edurt.datacap.parser.SqlParser

class MySQLParserModule : ParserModule() {
    override fun configure() {
        Multibinder.newSetBinder(binder(), SqlParser::class.java)
                .addBinding()
                .to(MySQLSqlParser::class.java)
    }
}
