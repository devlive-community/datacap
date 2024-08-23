package io.edurt.datacap.fs.cos

import com.google.inject.Guice.createInjector
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.TypeLiteral
import io.edurt.datacap.fs.Fs
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

class TencentCosModuleTest
{
    private val name = "TencentCos"
    private var injector: Injector? = null

    @Before
    fun before()
    {
        injector = createInjector(TencentCosModule())
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
