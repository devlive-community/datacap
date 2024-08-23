package io.edurt.datacap.fs.cos

import io.edurt.datacap.fs.Fs
import io.edurt.datacap.fs.FsRequest
import io.edurt.datacap.fs.FsResponse
import org.slf4j.LoggerFactory.getLogger

class TencentCosFs : Fs
{
    private val log = getLogger(TencentCosFs::class.java)

    override fun writer(request: FsRequest?): FsResponse
    {
        TODO("Not yet implemented")
    }

    override fun reader(request: FsRequest?): FsResponse
    {
        TODO("Not yet implemented")
    }
}
