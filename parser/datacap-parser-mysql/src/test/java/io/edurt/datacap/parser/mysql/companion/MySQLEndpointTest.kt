package io.edurt.datacap.parser.mysql.companion

import io.edurt.datacap.parser.mysql.companion.ast.ParserOptions
import org.junit.Test

class MySQLEndpointTest {
    @Test
    fun test() {
        val sql = "SELECT name AS name, age AS age, summer + 1 AS summer, SUM(summer) FROM a_table_name LIMIt 100"
        MySQLEndpoint().createStatement(sql, ParserOptions.DecimalLiteralTreatment.AS_DECIMAL)
    }
}
