package io.edurt.datacap.fs.alioss

import io.edurt.datacap.fs.FsRequest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader


class IOUtilsTest
{
    private var request = FsRequest()

    @Before
    fun before()
    {
        request.access = System.getProperty("access")
        request.secret = System.getProperty("secret")
        request.bucket = System.getProperty("bucket")
        request.fileName = "IOUtilsTest.kt"
        request.endpoint = System.getProperty("endpoint")
    }

    @Test
    fun copy()
    {
        val stream = FileInputStream("src/test/kotlin/io/edurt/datacap/fs/alioss/IOUtilsTest.kt")
        val result = IOUtils.copy(request, stream, "IOUtilsTest.kt")
        assertTrue(result != null)
    }

    @Test
    fun reader()
    {
        val stream = IOUtils.reader(request)
        val reader = BufferedReader(InputStreamReader(stream))
        while (true)
        {
            val line = reader.readLine() ?: break
            println(line.trimIndent())
        }
        assertNotNull(stream)
    }

    @Test
    fun delete()
    {
        assertTrue(IOUtils.delete(request))
    }
}
