package io.edurt.datacap.file.model

import java.io.ByteArrayOutputStream

class FileResponse
{
    var successful: Boolean? = true
    var message: String? = null
    var context: ByteArrayOutputStream? = null
}
