package io.edurt.datacap.common;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class OptionalUtilsTest
{
    private Optional<String> empty = Optional.of("");
    private Optional<String> notEmpty = Optional.of("s");

    @Test
    public void isEmpty()
    {
        Assert.assertTrue(OptionalUtils.isEmpty(empty));
        Assert.assertFalse(OptionalUtils.isEmpty(notEmpty));
    }

    @Test
    public void isNotEmpty()
    {
        Assert.assertFalse(OptionalUtils.isNotEmpty(empty));
        Assert.assertTrue(OptionalUtils.isNotEmpty(notEmpty));
    }
}
