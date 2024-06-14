package io.edurt.datacap.file.csv

import io.edurt.datacap.common.utils.DateUtils
import io.edurt.datacap.file.File
import io.edurt.datacap.file.FileConvert
import io.edurt.datacap.file.model.FileRequest
import io.edurt.datacap.file.model.FileResponse
import org.apache.commons.csv.CSVFormat
import org.apache.commons.io.FileUtils
import org.slf4j.LoggerFactory.getLogger
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.Objects.requireNonNull

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
        val response = FileResponse()
        val delimiter = ","
        try
        {
            requireNonNull("Stream must not be null")

            log.info("${name()} format stream start time [ ${DateUtils.now()} ]")
            request.stream
                ?.let {
                    BufferedReader(InputStreamReader(it, Charsets.UTF_8)).use { reader ->
                        response.headers = reader.readLine()
                            .split(delimiter)

                        val columns = mutableListOf<Any>()
                        reader.readLines()
                            .forEach { line -> columns.add(line.split(delimiter)) }
                        response.columns = columns
                        it.close()
                    }
                }
            log.info("${name()} format stream end time [ ${DateUtils.now()} ]")
            response.successful = true
        }
        catch (e: IOException)
        {
            response.successful = false
            response.message = e.message
        }
        return response
    }

    override fun writer(request: FileRequest): FileResponse
    {
        val response = FileResponse()
        try
        {
            log.info("${name()} writer start time [ ${DateUtils.now()} ]")
            response.headers = request.headers
            val file = FileConvert.formatFile(request, name())
            FileUtils.writeStringToFile(file, response.headers.joinToString(","), Charsets.UTF_8)

            val columns = mutableListOf<Any>()
            request.columns
                .forEach {
                    FileUtils.writeStringToFile(file, "\n", Charsets.UTF_8, true)
                    when (it)
                    {
                        is List<*> -> FileUtils.writeStringToFile(file, it.joinToString(separator = ","), Charsets.UTF_8, true)
                        else -> FileUtils.writeStringToFile(file, it.toString(), Charsets.UTF_8, true)
                    }
                }
            response.columns = columns
            log.info("${name()} writer end time [ ${DateUtils.now()} ]")
            response.successful = true
        }
        catch (e: IOException)
        {
            response.successful = false
            response.message = e.message
        }
        return response
    }

    override fun reader(request: FileRequest): FileResponse
    {
        TODO("Not yet implemented")
    }
}
