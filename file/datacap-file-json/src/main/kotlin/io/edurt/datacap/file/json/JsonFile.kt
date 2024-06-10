package io.edurt.datacap.file.json

import com.fasterxml.jackson.core.JsonEncoding
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.edurt.datacap.common.utils.DateUtils
import io.edurt.datacap.file.File
import io.edurt.datacap.file.FileConvert.formatFile
import io.edurt.datacap.file.model.FileRequest
import io.edurt.datacap.file.model.FileResponse
import org.slf4j.LoggerFactory.getLogger
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.Objects.requireNonNull

class JsonFile : File
{
    private val log = getLogger(this::class.java)

    override fun format(request: FileRequest): FileResponse
    {
        val response = FileResponse()
        try
        {
            log.info("${name()} format start time [ ${DateUtils.now()} ]")
            log.info("${name()} format headers start")
            response.headers = request.headers
            log.info("${name()} format headers end")

            log.info("${name()} format columns start")
            val mapper = ObjectMapper()
            val columns = mutableListOf<Any>()
            request.columns
                .forEach { column ->
                    val jsonNode = mapper.createObjectNode()
                    for (headerIndex in request.headers.indices)
                    {
                        val header = request.headers[headerIndex] as String
                        when (column)
                        {
                            is List<*> -> jsonNode.putPOJO(header, column[headerIndex])
                            else -> jsonNode.putPOJO(header, column)
                        }
                    }
                    columns.add(jsonNode)
                }
            response.columns = columns
            log.info("${name()} format columns end")

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
        try
        {
            requireNonNull("Stream must not be null")

            log.info("${name()} format stream start time [ ${DateUtils.now()} ]")
            val mapper = ObjectMapper()
            request.stream
                ?.let {
                    BufferedReader(InputStreamReader(it, Charsets.UTF_8)).use { reader ->
                        val jsonNode: JsonNode = mapper.readTree(reader)
                        log.info("${name()} format stream json node count [ ${jsonNode.size()} ]")

                        val headers = mutableListOf<Any>()
                        if (jsonNode.isArray && jsonNode.size() > 0)
                        {
                            jsonNode[0].fieldNames()
                                .forEachRemaining { headers.add(it) }
                        }
                        response.headers = headers

                        val columns = mutableListOf<Any>()
                        if (jsonNode.isArray)
                        {
                            jsonNode.elements()
                                .forEachRemaining { node ->
                                    val column = mutableMapOf<String, Any?>()
                                    node.fields()
                                        .forEachRemaining { field ->
                                            column[field.key] = field.value
                                        }
                                    columns.add(column)
                                }
                        }
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
            log.info("${name()} writer origin path [ ${request.path} ]")
            log.info("${name()} writer start time [ ${DateUtils.now()} ]")
            val file = formatFile(request, name())
            log.info("${name()} writer file absolute path [ ${file.absolutePath} ]")

            val factory = JsonFactory()
            factory.createGenerator(file, JsonEncoding.UTF8)
                .use { generator ->
                    generator.writeStartArray()
                    request.columns
                        .forEach { column ->
                            generator.writeStartObject()
                            for (headerIndex in request.headers.indices)
                            {
                                when (column)
                                {
                                    is List<*> -> generator.writeObjectField(request.headers[headerIndex] as String, column[headerIndex])
                                    else -> generator.writeObjectField(request.headers[headerIndex] as String, column.toString())
                                }
                            }
                            generator.writeEndObject()
                        }
                    generator.writeEndArray()
                }

            log.info("${name()} writer end time [ ${DateUtils.now()} ]")
            response.path = file.absolutePath
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
        val response = FileResponse()
        try
        {
            log.info("${name()} reader origin path [ ${request.path} ]")
            log.info("${name()} reader start time [ ${DateUtils.now()} ]")
            val file = formatFile(request, name())
            log.info("${name()} reader file absolute path [ ${file.absolutePath} ]")

            val mapper = ObjectMapper()
            val jsonNode: JsonNode = mapper.readTree(file)
            log.info("${name()} reader file json node count [ ${jsonNode.size()} ]")

            log.info("${name()} reader file headers start")
            val headers = mutableListOf<Any>()
            if (jsonNode.isArray && jsonNode.size() > 0)
            {
                jsonNode[0].fieldNames()
                    .forEachRemaining { headers.add(it) }
            }
            response.headers = headers
            log.info("${name()} reader file headers end")

            log.info("${name()} reader file columns start")
            val columns = mutableListOf<Any>()
            if (jsonNode.isArray)
            {
                jsonNode.elements()
                    .forEachRemaining { node ->
                        val column = mutableMapOf<String, Any?>()
                        node.fields()
                            .forEachRemaining { field ->
                                column[field.key] = field.value
                            }
                        columns.add(column)
                    }
            }
            response.columns = columns
            log.info("${name()} reader file columns end")
            response.successful = true
        }
        catch (e: Exception)
        {
            response.successful = false
            response.message = e.message
        }
        return response
    }
}
