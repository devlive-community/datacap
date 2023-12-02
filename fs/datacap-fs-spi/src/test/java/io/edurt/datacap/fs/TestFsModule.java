package io.edurt.datacap.fs;

import com.google.inject.multibindings.Multibinder;

public class TestFsModule
        extends FsModule
{
    protected void configure()
    {
        Multibinder.newSetBinder(this.binder(), Fs.class)
                .addBinding().to(TestFs.class);
    }
}
