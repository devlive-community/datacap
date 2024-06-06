package io.edurt.datacap.file

import com.google.inject.multibindings.Multibinder

class TestFileModule : FileModule()
{
    override fun configure()
    {
        Multibinder.newSetBinder(this.binder(), File::class.java)
            .addBinding()
            .to(TestFile::class.java)
    }
}
