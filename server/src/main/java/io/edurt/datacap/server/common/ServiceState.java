package io.edurt.datacap.server.common;

public enum ServiceState
{
    SOURCE_NOT_FOUND(1001, "Source does not exist"),
    PLUGIN_NOT_FOUND(2001, "Plugin dose not exists"),
    PLUGIN_EXECUTE_FAILED(2002, "Plugin execute failed"),
    REQUEST_VALID_ARGUMENT(3001, "The related parameters cannot be verified"),
    REQUEST_VALID_ARGUMENT_FORMAT(3002, "Unable to format related parameters"),
    REQUEST_VALID_ARGUMENT_LAYOUT(3003, "Related parameters cannot be resolved"),
    USER_NOT_FOUND(4001, "User dose not exists"),
    USER_ROLE_NOT_FOUND(4002, "User role dose not exists"),
    USER_UNAUTHORIZED(4003, "Insufficient current user permissions"),
    USER_EXISTS(4004, "User exists");

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
