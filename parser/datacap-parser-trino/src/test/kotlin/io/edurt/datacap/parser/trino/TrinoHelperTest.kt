package io.edurt.datacap.parser.trino

import io.edurt.datacap.parser.type.StatementType
import org.junit.Assert.assertTrue
import org.junit.Test

class TrinoHelperTest {

    @Test
    fun test() {
        assertTrue(TrinoHelper.parse("SELECT 1") !!.type == StatementType.SELECT)
    }

    @Test
    fun testFromTable() {
        println(TrinoHelper.parse("SELECT \"name\", \"age\" FROM \"a_table_name\""))
    }
}
