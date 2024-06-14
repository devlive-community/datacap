package io.edurt.datacap.file.csv

import io.edurt.datacap.common.utils.DateUtils
import io.edurt.datacap.file.File
import io.edurt.datacap.file.model.FileRequest
import io.edurt.datacap.file.model.FileResponse
import org.apache.commons.csv.CSVFormat
import org.slf4j.LoggerFactory.getLogger
import java.io.IOException

class CsvFile : File
{
    private val log = getLogger(this::class.java)

    override fun format(request: FileRequest): FileResponse
    {
        val response = FileResponse()
        try
        {
            val csvFormat = CSVFormat.newFormat(',')
            log.info("${name()} format start time [ ${DateUtils.now()} ]")
            response.headers = request.headers

            val columns = mutableListOf<Any>()
            request.columns
                .forEach { columns.add(csvFormat.format(it)) }
            response.columns = columns
            log.info("${name()} format end time [ ${DateUtils.now()} ]")
            response.successful = true
        }
        catch (e: IOException)
        {
            response.successful = false
            response.message = e.message
        }
        return response
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
