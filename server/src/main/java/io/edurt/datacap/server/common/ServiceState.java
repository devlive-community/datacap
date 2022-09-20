package io.edurt.datacap.server.common;

public enum ServiceState
{
    SOURCE_NOT_FOUND(1001, "Source does not exist"),
    PLUGIN_NOT_FOUND(2001, "Plugin dose not exists");

    private Integer code;
    private String value;

    ServiceState(Integer code, String value)
    {
        this.code = code;
        this.value = value;
    }

    public Integer getCode()
    {
        return code;
    }

    public String getValue()
    {
        return value;
    }
}
