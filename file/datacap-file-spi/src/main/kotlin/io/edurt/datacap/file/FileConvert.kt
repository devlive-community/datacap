package io.edurt.datacap.file

import io.edurt.datacap.common.utils.UrlUtils
import io.edurt.datacap.file.model.FileRequest
import java.util.*

object FileConvert
{
    /**
     * Formats the file path by combining the name from the given FileRequest and the plugin name.
     *
     * @param request the FileRequest object containing the name of the file
     * @param plugin the name of the plugin
     * @return the formatted file path as a string
     */
    @JvmStatic
    fun formatFilePath(request: FileRequest, plugin: String): String
    {
        return listOf(request.name, plugin.lowercase(Locale.getDefault()))
            .joinToString(separator = ".")
    }

    /**
     * Formats the file path by combining the name from the given FileRequest and the plugin name,
     * and returns a java.io.File object representing the formatted path.
     *
     * @param request the FileRequest object containing the name of the file
     * @param plugin the name of the plugin
     * @return a java.io.File object representing the formatted file path
     */
    @JvmStatic
    fun formatFile(request: FileRequest, plugin: String): java.io.File
    {
        return java.io.File(
            UrlUtils.fixUrl(
                listOf(request.path, formatFilePath(request, plugin))
                    .joinToString(separator = "/")
            )
        )
    }
}
