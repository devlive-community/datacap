package io.edurt.datacap.file

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.TypeLiteral
import org.junit.Assert
import org.junit.Test

class FileModuleTest
{
    private val injector: Injector = Guice.createInjector(FileManager())

    @Test
    fun test()
    {
        injector.getInstance(Key.get(object : TypeLiteral<Set<File>>()
        {}))
            .stream()
            .findFirst()
            .ifPresent {
                Assert.assertEquals("Test", it.name())
            }
    }
}
