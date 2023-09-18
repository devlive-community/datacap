package io.edurt.datacap.common.enums;

import lombok.Getter;

public enum NodeType
{
    CATALOG("CATALOG"),
    SCHEMA("SCHEMA"),
    CHARSET("CHARSET");

    @Getter
    private String value;

    NodeType(String value)
    {
        this.value = value;
    }
}
