package io.edurt.datacap.file.model

class FileResponse
{
    private var _headers: List<Any> = listOf()
    private var _columns: List<Any> = listOf()

    var successful: Boolean? = true
    var message: String? = null
    var path: String? = null

    var headers: List<Any>
        get() = _headers.toList()
        set(value)
        {
            _headers = value.toMutableList()
        }

    var columns: List<Any>
        get() = _columns.toList()
        set(value)
        {
            _columns = value.toMutableList()
        }
}
