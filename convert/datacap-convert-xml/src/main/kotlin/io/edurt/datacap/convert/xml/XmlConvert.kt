package io.edurt.datacap.convert.xml

import com.fasterxml.jackson.core.JsonEncoding
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import io.edurt.datacap.common.utils.DateUtils
import io.edurt.datacap.convert.Convert
import io.edurt.datacap.convert.FileConvert.formatFile
import io.edurt.datacap.convert.model.ConvertRequest
import io.edurt.datacap.convert.model.ConvertResponse
import org.slf4j.LoggerFactory.getLogger

class XmlConvert : Convert
{
    private val log = getLogger(this::class.java)
    val root = "Root"
    val node = "Node"

    override fun format(request: ConvertRequest): ConvertResponse
    {
        TODO("Not yet implemented")
    }

    override fun formatStream(request: ConvertRequest): ConvertResponse
    {
        TODO("Not yet implemented")
    }

    override fun writer(request: ConvertRequest): ConvertResponse
    {
        val response = ConvertResponse()
        try
        {
            log.info("${name()} format start time [ ${DateUtils.now()} ]")
            val file = formatFile(request, name())
            log.info("${name()} writer file absolute path [ ${file.absolutePath} ]")

            val factory = XmlFactory()
            factory.createGenerator(file, JsonEncoding.UTF8)
                .use { generator ->
                    generator.codec = XmlMapper()
                    val staxWriter = generator.staxWriter
                    staxWriter.writeStartElement(root)
                    request.columns
                        .forEach { column ->
                            staxWriter.writeStartElement(node)
                            for (headerIndex in request.headers.indices)
                            {
                                when (column)
                                {
                                    is List<*> -> staxWriter.writeAttribute(request.headers[headerIndex].toString(), column[headerIndex].toString())
                                    else -> throw UnsupportedOperationException("Unsupported column type")
                                }
                            }
                            staxWriter.writeEndElement()
                        }
                    staxWriter.writeEndElement()
                }
            log.info("${name()} format end time [ ${DateUtils.now()} ]")
            response.path = file.absolutePath
            response.successful = true
        }
        catch (e: Exception)
        {
            e.printStackTrace()
            response.successful = false
            response.message = e.message
        }
        return response
    }

    override fun reader(request: ConvertRequest): ConvertResponse
    {
        TODO("Not yet implemented")
    }
}
