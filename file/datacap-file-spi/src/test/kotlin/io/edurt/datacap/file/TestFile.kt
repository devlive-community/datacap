package io.edurt.datacap.file

import io.edurt.datacap.file.model.FileRequest
import io.edurt.datacap.file.model.FileResponse

class TestFile : File
{
    override fun format(request: FileRequest): FileResponse
    {
        TODO("Not yet implemented")
    }

    override fun writer(request: FileRequest): FileResponse
    {
        TODO("Not yet implemented")
    }

    override fun reader(request: FileRequest): FileResponse
    {
        TODO("Not yet implemented")
    }
}
