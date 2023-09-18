package io.edurt.datacap.common.enums;

import lombok.Getter;

public enum NodeType
{
    CATALOG("CATALOG"),
    SCHEMA("SCHEMA"),
    CHARSET("CHARSET"),
    TABLE("TABLE"),
    TYPE("TYPE"),
    ENGINE("ENGINE"),
    FORMAT("FORMAT"),
    ROWS("ROWS"),
    CREATE_TIME("CREATE_TIME"),
    UPDATE_TIME("UPDATE_TIME"),
    COLLATION("COLLATION"),
    COMMENT("COMMENT"),
    DEFAULT("DEFAULT"),
    POSITION("POSITION"),
    NULLABLE("NULLABLE"),
    MAXIMUM_LENGTH("MAXIMUM_LENGTH"),
    OCTET_LENGTH("OCTET_LENGTH"),
    KEY("KEY"),
    PRIVILEGES("PRIVILEGES"),
    COLUMN("COLUMN");

    @Getter
    private String value;

    NodeType(String value)
    {
        this.value = value;
    }
}
