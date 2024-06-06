package io.edurt.datacap.file

import com.google.inject.Guice
import org.junit.Assert
import org.junit.Test

class FileFilterTest
{
    private val injector = Guice.createInjector(FileManager())

    @Test
    fun testFindNotify()
    {
        Assert.assertNotNull(
            FileFilter.findNotify(injector, "Test")
                .get()
        )
    }
}
