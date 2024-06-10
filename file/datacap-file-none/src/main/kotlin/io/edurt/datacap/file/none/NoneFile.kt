package io.edurt.datacap.file.none

import io.edurt.datacap.file.File
import io.edurt.datacap.file.model.FileRequest
import io.edurt.datacap.file.model.FileResponse

class NoneFile : File
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
