package io.edurt.datacap.parser.trino

import com.google.inject.multibindings.Multibinder
import io.edurt.datacap.parser.Parser
import io.edurt.datacap.parser.ParserModule

class TrinoParserModule : ParserModule() {
    override fun configure() {
        Multibinder.newSetBinder(binder(), Parser::class.java)
                .addBinding()
                .to(TrinoParser::class.java)
    }
}
