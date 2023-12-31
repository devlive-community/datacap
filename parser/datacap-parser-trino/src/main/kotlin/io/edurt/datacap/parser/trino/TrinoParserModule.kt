package io.edurt.datacap.parser.trino

import com.google.inject.multibindings.Multibinder
import io.edurt.datacap.parser.SqlParser
import io.edurt.datacap.parser.ParserModule

class TrinoParserModule : ParserModule() {
    override fun configure() {
        Multibinder.newSetBinder(binder(), SqlParser::class.java)
                .addBinding()
                .to(TrinoSqlParser::class.java)
    }
}
