package io.edurt.datacap.file.txt

import com.google.inject.Guice
import com.google.inject.Injector
import io.edurt.datacap.file.FileFilter
import io.edurt.datacap.file.FileManager
import io.edurt.datacap.file.model.FileRequest
import org.junit.Before
import org.junit.Test

class TxtFileTest
{
    private val name = "Txt"
    private var injector: Injector? = null
    private val request: FileRequest = FileRequest()

    @Before
    fun before()
    {
        injector = Guice.createInjector(FileManager())

        request.name = "test.txt"
        request.content = "Test Message"
        request.path = System.getProperty("user.dir")
    }

    @Test
    fun test()
    {
        injector?.let {
            FileFilter.findNotify(it, name)
                .ifPresent {
                    println(it.writer(request))
                }
        }
    }
}
