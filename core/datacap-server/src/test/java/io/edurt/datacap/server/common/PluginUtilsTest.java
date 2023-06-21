package io.edurt.datacap.server.common;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.edurt.datacap.service.common.PluginUtils;
import io.edurt.datacap.service.configure.IConfigure;
import org.junit.Assert;
import org.junit.Test;

@SuppressFBWarnings(value = {"NP_NULL_PARAM_DEREF_NONVIRTUAL"},
        justification = "I prefer to suppress these FindBugs warnings")
public class PluginUtilsTest
{
    private String resource = "default.yaml";

    @Test
    public void loadConfigure()
    {
        IConfigure configure = PluginUtils.loadYamlConfigure("JDBC", "MySQL", resource, null);
        Assert.assertNotNull(configure);
    }
}