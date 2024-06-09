package io.edurt.datacap.file.model

class FileRequest
{
    private var _headers: List<Any> = listOf()
    private var _columns: List<Any> = listOf()

    var name: String? = null
    var path: String? = null

    // Currently only valid for txt format
    var delimiter: String? = null

    // The format type of the original data
    var format: String? = null

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
