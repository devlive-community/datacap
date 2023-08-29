package io.edurt.datacap.captcha;

import org.junit.Assert;
import org.junit.Test;

public class CaptchaUtilsTest
{

    @Test
    public void testGetBackgroundColor()
    {
        Assert.assertNotNull(CaptchaUtils.getBackgroundColor());
    }

    @Test
    public void testGenerateCalculateImage()
    {
        Assert.assertNotNull(CaptchaUtils.generateCalculateImage());
    }
}