package io.edurt.datacap.parser.trino

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.TypeLiteral
import io.edurt.datacap.parser.Parser
import io.edurt.datacap.parser.ParserManager
import org.junit.Assert.assertEquals
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class TrinoParserModuleTest {
    private val log: Logger = LoggerFactory.getLogger(TrinoParserModuleTest::class.java)
    private val injector: Injector = Guice.createInjector(ParserManager())

    @Test
    fun test() {
        val optional: Optional<Parser> = injector.getInstance(Key.get(object : TypeLiteral<Set<Parser>>() {}))
                .stream()
                .findFirst()

        optional.ifPresent {
            log.info("Module name: [ {} ]", it.name())
            assertEquals(it.name(), "Trino")
        }
    }
}
