package io.edurt.datacap.server.common;

import io.edurt.datacap.common.utils.ResourceUtils;
import org.junit.Assert;
import org.junit.Test;

public class ResourceUtilsTest
{
    private String resource = "default.yaml";
    private String resource2 = "empty.json";

    @Test
    public void loadResourceToString()
    {
        Assert.assertNull(ResourceUtils.loadResourceToString(resource2, false));
        Assert.assertNotNull(ResourceUtils.loadResourceToString(resource, false));
    }

    @Test
    public void checkExists()
    {
        System.out.println(ResourceUtils.checkExists(resource));
        Assert.assertTrue(ResourceUtils.checkExists(resource));
        Assert.assertFalse(ResourceUtils.checkExists(resource2));
    }
}