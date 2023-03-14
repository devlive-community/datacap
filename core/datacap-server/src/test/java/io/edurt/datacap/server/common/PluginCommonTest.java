package io.edurt.datacap.server.common;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.server.plugin.configure.IConfigure;
import org.junit.Assert;
import org.junit.Test;

@SuppressFBWarnings(value = {"NP_NULL_PARAM_DEREF_NONVIRTUAL"},
        justification = "I prefer to suppress these FindBugs warnings")
public class PluginCommonTest
{
    private String resource = "default.yaml";

    @Test
    public void loadConfigure()
    {
        IConfigure configure = PluginCommon.loadYamlConfigure("JDBC", "MySQL", resource, null);
        Assert.assertNotNull(configure);
    }
}