package io.edurt.datacap.fs.qiniu

import io.edurt.datacap.fs.FsRequest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.FileInputStream

class IOUtilsTest {
    private var request = FsRequest()

    @Before
    fun before() {
        request.access = System.getProperty("access")
        request.secret = System.getProperty("secret")
        request.bucket = System.getProperty("bucket")
        request.fileName = "IOUtilsTest.kt"
        request.endpoint = System.getProperty("endpoint")
    }

    @Test
    fun copy() {
        val stream = FileInputStream("src/test/kotlin/io/edurt/datacap/fs/qiniu/IOUtilsTest.kt")
        val result = IOUtils.copy(request, stream, "IOUtilsTest.kt")
        assertTrue(result != null)
    }

    @Test
    fun reader() {
        assertNotNull(IOUtils.reader(request))
    }

    @Test
    fun delete() {
        assertTrue(IOUtils.delete(request))
    }
}
