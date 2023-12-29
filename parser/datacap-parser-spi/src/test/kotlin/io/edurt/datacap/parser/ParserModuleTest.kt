package io.edurt.datacap.parser

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.TypeLiteral
import org.junit.Assert
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class ParserModuleTest {
    private val log: Logger = LoggerFactory.getLogger(TestParser::class.java)
    private var injector: Injector = Guice.createInjector(ParserManager())

    @Test
    fun test() {
        val optional: Optional<Parser> = injector.getInstance(Key.get(object : TypeLiteral<Set<Parser>>() {}))
                .stream()
                .findFirst()

        optional.ifPresent {
            log.info("Parser response: {}", optional.get().parse("SELECT 1"))
            Assert.assertEquals("Test", optional.get().name())
        }
    }
}
