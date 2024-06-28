package io.edurt.datacap.convert

import io.edurt.datacap.convert.model.ConvertRequest
import io.edurt.datacap.convert.model.ConvertResponse

class TestConvert : Convert
{
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
        TODO("Not yet implemented")
    }

    override fun reader(request: ConvertRequest): ConvertResponse
    {
        TODO("Not yet implemented")
    }
}
