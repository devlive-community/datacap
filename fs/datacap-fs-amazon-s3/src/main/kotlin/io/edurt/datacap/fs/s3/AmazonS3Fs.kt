package io.edurt.datacap.fs.s3

import io.edurt.datacap.fs.Fs
import io.edurt.datacap.fs.FsRequest
import io.edurt.datacap.fs.FsResponse
import org.slf4j.LoggerFactory.getLogger

class AmazonS3Fs : Fs
{
    private val logger = getLogger(AmazonS3Fs::class.java)

    override fun writer(request: FsRequest?): FsResponse
    {
        TODO("Not yet implemented")
    }

    override fun reader(request: FsRequest?): FsResponse
    {
        TODO("Not yet implemented")
    }
}
