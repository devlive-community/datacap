package io.edurt.datacap.fs.minio

import com.google.inject.Guice.createInjector
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.TypeLiteral
import io.edurt.datacap.fs.Fs
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MinIOModuleTest
{
    private val name = this.javaClass.simpleName.replace("ModuleTest", "")
    private var injector: Injector? = null

    @Before
    fun before()
    {
        injector = createInjector(MinIOModule())
    }

    @Test
    fun test()
    {
        val fs: Fs? = injector?.getInstance(Key.get(object : TypeLiteral<Set<Fs?>?>()
        {}))
            ?.first { v -> v?.name().equals(name) }
        assertTrue(fs != null)
    }
}
