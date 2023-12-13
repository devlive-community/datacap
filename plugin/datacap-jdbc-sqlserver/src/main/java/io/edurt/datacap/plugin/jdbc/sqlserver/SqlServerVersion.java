package io.edurt.datacap.plugin.jdbc.sqlserver;

public enum SqlServerVersion
{
    V2019("2019"),
    V2000("2000"),
    V2022("2022");

    private String version;

    SqlServerVersion(String version)
    {
        this.version = version;
    }

    public String getVersion()
    {
        return this.version;
    }
}
