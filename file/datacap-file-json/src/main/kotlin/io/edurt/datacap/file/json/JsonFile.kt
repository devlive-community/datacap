package io.edurt.datacap.file.json

import com.fasterxml.jackson.core.JsonEncoding
import com.fasterxml.jackson.core.JsonFactory
import io.edurt.datacap.common.utils.DateUtils
import io.edurt.datacap.file.File
import io.edurt.datacap.file.FileConvert.formatFile
import io.edurt.datacap.file.model.FileRequest
import io.edurt.datacap.file.model.FileResponse
import org.slf4j.LoggerFactory.getLogger

class JsonFile : File {
    private val log = getLogger(this::class.java)

    override fun writer(request: FileRequest): FileResponse {
        val response = FileResponse()
        try {
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
                            for (headerIndex in request.headers.indices) {
                                when (column) {
                                    is List<*> -> generator.writeObjectField(request.headers[headerIndex] as String, column[headerIndex])
                                    else -> generator.writeObjectField(request.headers[headerIndex] as String, column.toString())
                                }
                            }
                            generator.writeEndObject()
                        }
                    generator.writeEndArray()
                }

            log.info("${name()} writer end time [ ${DateUtils.now()} ]")
            response.successful = true
        } catch (e: Exception) {
            response.successful = false
            response.message = e.message
        }
        return response
    }

    override fun reader(request: FileRequest): FileResponse {
        TODO("Not yet implemented")
    }
}
