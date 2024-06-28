package io.edurt.datacap.convert

import io.edurt.datacap.convert.model.ConvertRequest
import io.edurt.datacap.convert.model.ConvertResponse

interface Convert
{
    fun name(): String
    {
        return this.javaClass
            .simpleName
            .removeSuffix("File")
    }

    fun description(): String
    {
        return "Integrate ${name()} file"
    }

    fun format(request: ConvertRequest): ConvertResponse

    fun formatStream(request: ConvertRequest): ConvertResponse

    fun writer(request: ConvertRequest): ConvertResponse

    fun reader(request: ConvertRequest): ConvertResponse
}
