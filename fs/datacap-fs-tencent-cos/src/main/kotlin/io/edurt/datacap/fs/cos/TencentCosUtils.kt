package io.edurt.datacap.fs.cos

import com.qcloud.cos.COSClient
import com.qcloud.cos.ClientConfig
import com.qcloud.cos.auth.BasicCOSCredentials
import com.qcloud.cos.model.ObjectMetadata
import com.qcloud.cos.model.PutObjectRequest
import com.qcloud.cos.region.Region
import io.edurt.datacap.fs.FsRequest
import java.io.InputStream

class TencentCosUtils
{
    companion object
    {
        private fun getClient(request: FsRequest): COSClient
        {
            val cred = BasicCOSCredentials(request.access, request.secret)
            val region: Region = Region(request.endpoint)
            val clientConfig = ClientConfig(region)
            return COSClient(cred, clientConfig)
        }

        @JvmStatic
        fun copy(request: FsRequest, stream: InputStream, fileName: String): String?
        {
            try
            {
                val client = getClient(request)

                val objectMetadata = ObjectMetadata()
                objectMetadata.contentLength = stream.available().toLong()
                objectMetadata.cacheControl = "no-cache"
                val putObject = PutObjectRequest(request.bucket, request.fileName, stream, objectMetadata)

                val response = client.putObject(putObject)
                return response.requestId
            }
            catch (e: Exception)
            {
                throw RuntimeException(e)
            }
        }
    }
}
