package io.edurt.datacap.file.json

import com.google.inject.Guice.createInjector
import com.google.inject.Injector
import io.edurt.datacap.file.FileFilter
import io.edurt.datacap.file.FileManager
import io.edurt.datacap.file.model.FileRequest
import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory.getLogger
import kotlin.test.assertTrue

class JsonFileTest
{
    private val log = getLogger(this::class.java)
    private val name = "Json"
    private var injector: Injector? = null
    private val request: FileRequest = FileRequest()

    @Before
    fun before()
    {
        injector = createInjector(FileManager())

        request.name = "test"
        request.path = System.getProperty("user.dir")
        request.headers = listOf("name", "age")

        val l1 = listOf("Test", 12)
        val l2 = listOf("Test1", 121)
        request.columns = listOf(l1, l2)
    }

    @Test
    fun testFormat()
    {
        injector?.let { injector ->
            FileFilter.findNotify(injector, name)
                .ifPresent { file ->
                    val response = file.format(request)
                    log.info("headers: [ ${response.headers} ]")
                    response.columns
                        .let { columns ->
                            columns.forEachIndexed { index, line ->
                                log.info("index: [ $index ], line: [ $line ]")
                            }
                        }

                    assertTrue {
                        response.successful == true
                    }
                }
        }
    }

    @Test
    fun testWriter()
    {
        injector?.let { injector ->
            FileFilter.findNotify(injector, name)
                .ifPresent { file ->
                    assertTrue {
                        file.writer(request)
                            .successful == true
                    }
                }
        }
    }

    @Test
    fun testReader()
    {
        injector?.let { injector ->
            FileFilter.findNotify(injector, name)
                .ifPresent { file ->
                    val response = file.reader(request)
                    log.info("headers: ${response.headers}")
                    response.columns
                        .forEach {
                            log.info("columns: $it")
                        }
                    assertTrue {
                        response.successful == true
                    }
                }
        }
    }
}
