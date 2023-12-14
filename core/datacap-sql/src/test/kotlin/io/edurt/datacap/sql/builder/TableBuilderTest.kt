package io.edurt.datacap.sql.builder

import org.junit.Assert
import org.junit.Test

class TableBuilderTest {
    private val tableName: String = "TestTable"
    private val columns: List<String> = listOf("id int(32) primary key auto_increment", "name varchar(32) comment 'name'", "age varchar(200) not null default 'xxx'");

    @Test
    fun testCreateTable() {
        TableBuilder.BEGIN()
        TableBuilder.CREATE_TABLE(tableName)
        TableBuilder.COLUMNS(columns)
        Assert.assertEquals(TableBuilder.SQL(), "CREATE TABLE TestTable\n" +
                "(\n" +
                "\tid int(32) primary key auto_increment,\n" +
                "\tname varchar(32) comment 'name',\n" +
                "\tage varchar(200) not null default 'xxx'\n" +
                ")")
    }
}
