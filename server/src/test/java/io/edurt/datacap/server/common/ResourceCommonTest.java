package io.edurt.datacap.server.common;

import org.junit.Assert;
import org.junit.Test;

public class ResourceCommonTest
{
    private String resource = "default.json";
    private String resource2 = "empty.json";

    @Test
    public void loadResourceToString()
    {
        Assert.assertNull(ResourceCommon.loadResourceToString(resource2, false));
        Assert.assertNotNull(ResourceCommon.loadResourceToString(resource, false));
    }

    @Test
    public void checkExists()
    {
        System.out.println(ResourceCommon.checkExists(resource));
        Assert.assertTrue(ResourceCommon.checkExists(resource));
        Assert.assertFalse(ResourceCommon.checkExists(resource2));
    }
}