package io.edurt.datacap.fs;

import com.google.inject.AbstractModule;

public abstract class FsModule
        extends AbstractModule
{
    String name()
    {
        return this.getClass()
                .getSimpleName()
                .replace("Fs", "")
                .replace("Module", "");
    }
}
