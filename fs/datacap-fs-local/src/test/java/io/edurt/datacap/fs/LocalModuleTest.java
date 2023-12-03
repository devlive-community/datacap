package io.edurt.datacap.fs;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

public class LocalModuleTest
{
    private Injector injector;

    @Before
    public void before()
    {
        injector = Guice.createInjector(new FsManager());
    }

    @Test
    public void test()
    {
        Assert.assertNotNull(injector.getInstance(Key.get(new TypeLiteral<Set<Fs>>() {})));
    }
}