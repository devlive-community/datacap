package io.edurt.datacap.fs.s3

import io.edurt.datacap.fs.FsRequest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.FileInputStream

class AmazonS3UtilsTest
{
    private var request = FsRequest()

    @Before
    fun before()
    {
        request.access = System.getProperty("access")
        request.secret = System.getProperty("secret")
        request.bucket = System.getProperty("bucket")
        request.fileName = "AmazonS3FsTest.kt"
        request.endpoint = System.getProperty("endpoint")
    }

    @Test
    fun copy()
    {
        val stream = FileInputStream("src/test/kotlin/io/edurt/datacap/fs/s3/AmazonS3FsTest.kt")
        val result = AmazonS3Utils.copy(request, stream, "AmazonS3FsTest.kt")
        assertTrue(result != null)
    }

    @Test
    fun reader()
    {
        assertNotNull(AmazonS3Utils.reader(request))
    }

    @Test
    fun delete()
    {
        assertTrue(AmazonS3Utils.delete(request))
    }
}
