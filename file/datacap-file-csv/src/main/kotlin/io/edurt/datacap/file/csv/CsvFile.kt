package io.edurt.datacap.file.csv

import io.edurt.datacap.file.File
import io.edurt.datacap.file.model.FileRequest
import io.edurt.datacap.file.model.FileResponse

class CsvFile : File
{
    override fun format(request: FileRequest): FileResponse
    {
        TODO("Not yet implemented")
    }

    override fun formatStream(request: FileRequest): FileResponse
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
