package io.edurt.datacap.fs.s3

import com.google.inject.Guice
import com.google.inject.Injector
import io.edurt.datacap.fs.FsManager
import io.edurt.datacap.fs.FsRequest
import org.junit.Before
import org.slf4j.LoggerFactory

class AmazonS3FsTest
{
    private val log = LoggerFactory.getLogger(AmazonS3FsTest::class.java)
    private val name = "AmazonS3"
    private var request = FsRequest()
    private var injector: Injector? = null

    @Before
    fun before()
    {
        request.access = System.getProperty("access")
        request.secret = System.getProperty("secret")
        request.bucket = System.getProperty("bucket")
        request.fileName = "TencentCosFsTest.kt"
        request.endpoint = System.getProperty("endpoint")

        injector = Guice.createInjector(FsManager())
    }
}
