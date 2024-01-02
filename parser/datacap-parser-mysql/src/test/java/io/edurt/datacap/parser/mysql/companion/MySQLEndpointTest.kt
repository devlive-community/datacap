package io.edurt.datacap.parser.mysql.companion

import io.edurt.datacap.parser.mysql.companion.ast.ParserOptions
import org.junit.Test

class MySQLEndpointTest {
    @Test
    fun test() {
        MySQLEndpoint().createStatement("SELECT 1", ParserOptions.DecimalLiteralTreatment.AS_DECIMAL)
    }
}
