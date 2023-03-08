package io.edurt.datacap.lib.shell;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class ShellCommander
{
    protected final ShellConfigure configure;

    public ShellCommander(ShellConfigure configure)
    {
        this.configure = configure;
    }

    protected String getShellType()
    {
        return "/bin/sh";
    }

    protected Map<String, String> getEnvironment()
    {
        Map<String, String> environment = new HashMap<>();
        Properties properties = System.getProperties();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            environment.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
        environment.putAll(System.getenv());
        return environment;
    }

    public abstract ShellResponse execute();
}
