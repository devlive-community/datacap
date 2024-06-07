package io.edurt.datacap.file.model

class FileRequest
{
    var name: String? = null
    var path: String? = null
    var headers: List<Any>? = null
    var columns: List<Any>? = null

    // Currently only valid for txt format
    var delimiter: String? = null

    // The format type of the original data
    var format: String? = null
}
