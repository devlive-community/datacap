package io.edurt.datacap.fs.minio

import io.edurt.datacap.fs.FsRequest
import io.minio.MinioClient
import io.minio.PutObjectArgs
import java.io.InputStream

class MinIOUtils
{
    companion object
    {
        private fun getClient(request: FsRequest): MinioClient
        {
            return MinioClient.builder()
                .endpoint(request.endpoint)
                .credentials(request.access, request.secret)
                .build()
        }

        @JvmStatic
        fun copy(request: FsRequest, stream: InputStream, fileName: String): String?
        {
            val client = getClient(request)

            try
            {
                val response = client.putObject(
                    PutObjectArgs.builder()
                        .stream(stream, stream.available().toLong(), - 1)
                        .bucket(request.bucket)
                        .`object`(request.fileName)
                        .build()
                )
                return response.versionId()
            }
            catch (e: Exception)
            {
                throw RuntimeException(e)
            }
        }
    }
}
