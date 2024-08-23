package io.edurt.datacap.fs.cos

import io.edurt.datacap.fs.Fs
import io.edurt.datacap.fs.FsRequest
import io.edurt.datacap.fs.FsResponse
import io.edurt.datacap.fs.cos.TencentCosUtils.Companion.copy
import org.slf4j.LoggerFactory.getLogger
import java.io.File
import java.lang.String.join

class TencentCosFs : Fs
{
    private val log = getLogger(TencentCosFs::class.java)

    override fun writer(request: FsRequest?): FsResponse
    {
        requireNotNull(request) { "request must not be null" }

        log.info("TencentCos writer origin path [ {} ]", request.fileName)
        val targetPath = join(File.separator, request.endpoint, request.bucket, request.fileName)
        val response = FsResponse.builder()
            .origin(request.fileName)
            .remote(targetPath)
            .successful(true)
            .build()
        log.info("TencentCos writer target path [ {} ]", request.fileName)
        try
        {
            val key = copy(request, request.stream, request.fileName)
            response.remote = key
            log.info("TencentCos writer [ {} ] successfully", key)
        }
        catch (e: Exception)
        {
            log.error("TencentCos writer error", e)
            response.isSuccessful = false
            response.message = e.message
        }
        return response
    }

    override fun reader(request: FsRequest?): FsResponse
    {
        TODO("Not yet implemented")
    }
}
