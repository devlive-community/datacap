package io.edurt.datacap.fs.minio

import io.edurt.datacap.fs.FsRequest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.FileInputStream

class MinIOUtilsTest
{
    private val name = "MinIOUtilsTest.kt"
    private var request = FsRequest()

    @Before
    fun before()
    {
        request.access = System.getProperty("access")
        request.secret = System.getProperty("secret")
        request.bucket = System.getProperty("bucket")
        request.fileName = name
        request.endpoint = System.getProperty("endpoint")
    }

    @Test
    fun copy()
    {
        val stream = FileInputStream("src/test/kotlin/io/edurt/datacap/fs/minio/${name}")
        val result = MinIOUtils.copy(request, stream, name)
        assertTrue(result != null)
    }
}
