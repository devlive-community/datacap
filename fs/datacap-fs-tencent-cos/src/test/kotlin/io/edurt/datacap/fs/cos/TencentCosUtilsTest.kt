package io.edurt.datacap.fs.cos

import io.edurt.datacap.fs.FsRequest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.FileInputStream

class TencentCosUtilsTest
{
    private var request = FsRequest()

    @Before
    fun before()
    {
        request.access = System.getProperty("access")
        request.secret = System.getProperty("secret")
        request.bucket = System.getProperty("bucket")
        request.fileName = "TencentCosFsTest.kt"
        request.endpoint = System.getProperty("endpoint")
    }

    @Test
    fun copy()
    {
        val stream = FileInputStream("src/test/kotlin/io/edurt/datacap/fs/cos/TencentCosFsTest.kt")
        val result = TencentCosUtils.copy(request, stream, "TencentCosFsTest.kt")
        assertTrue(result != null)
    }

    @Test
    fun reader()
    {
        assertNotNull(TencentCosUtils.reader(request))
    }

    @Test
    fun delete()
    {
        assertTrue(TencentCosUtils.delete(request))
    }
}
