package io.edurt.datacap.server.common;

public enum ServiceState
{
    SOURCE_NOT_FOUND(1001, "Source does not exist"),
    PLUGIN_NOT_FOUND(2001, "Plugin dose not exists"),
    PLUGIN_EXECUTE_FAILED(2002, "Plugin execute failed"),
    REQUEST_VALID_ARGUMENT(3001, "The related parameters cannot be verified"),
    REQUEST_VALID_ARGUMENT_FORMAT(3002, "Unable to format related parameters"),
    REQUEST_VALID_ARGUMENT_LAYOUT(3003, "Related parameters cannot be resolved");

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
