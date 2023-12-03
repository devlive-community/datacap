package io.edurt.datacap.fs;

import com.google.inject.multibindings.Multibinder;

public class LocalModule
        extends FsModule
{
    @Override
    protected void configure()
    {
        Multibinder.newSetBinder(this.binder(), Fs.class)
                .addBinding().to(LocalFs.class);
    }
}
