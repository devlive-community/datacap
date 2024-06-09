package io.edurt.datacap.file.txt

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.TypeLiteral
import io.edurt.datacap.file.File
import io.edurt.datacap.file.FileManager
import org.junit.Assert
import org.junit.Test

class TxtModuleTest
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
                Assert.assertEquals("Txt", it.name())
            }
    }
}
