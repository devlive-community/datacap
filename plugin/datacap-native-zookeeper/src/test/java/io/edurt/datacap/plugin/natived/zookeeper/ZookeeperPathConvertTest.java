package io.edurt.datacap.plugin.natived.zookeeper;

import org.junit.Assert;
import org.junit.Test;

public class ZookeeperPathConvertTest
{
    @Test
    public void toPath()
    {
        String path = "all";
        Assert.assertEquals(ZookeeperPathConvert.toPath(path), ZookeeperPathConvert.start);
        path = "a.b";
        Assert.assertEquals(ZookeeperPathConvert.toPath(path), "/a/b");
        path = "a.b.`sd.dd`.`d_dd`";
        Assert.assertEquals(ZookeeperPathConvert.toPath(path), "/a/b/sd.dd/d_dd");
        path = "`sd.dd`.a.n.`d_dd`";
        Assert.assertEquals(ZookeeperPathConvert.toPath(path), "/sd.dd/a/n/d_dd");
    }
}
