package com.kaivo.app.util

import com.kaivo.app.data.ClipItem
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStream

enum class ExportFormat(val extension: String, val mimeType: String) {
    TXT("txt", "text/plain"),
    JSON("json", "application/json")
}

object ExportUtil {

    fun suggestedFileName(format: ExportFormat): String =
        "kaivo_export.${format.extension}"

    fun buildTxt(items: List<ClipItem>): String {
        return items.joinToString(separator = "\n\n") { item ->
            "[${DateUtil.formatForExport(item.createdAt)}]\n${item.content}"
        }
    }

    fun buildJson(items: List<ClipItem>): String {
        val array = JSONArray()
        items.forEach { item ->
            val obj = JSONObject()
            obj.put("id", item.id)
            obj.put("content", item.content)
            obj.put("createdAt", item.createdAt)
            obj.put("createdAtFormatted", DateUtil.formatForExport(item.createdAt))
            obj.put("pinned", item.isPinned)
            array.put(obj)
        }
        return array.toString(2)
    }

    fun write(outputStream: OutputStream, items: List<ClipItem>, format: ExportFormat) {
        val content = when (format) {
            ExportFormat.TXT -> buildTxt(items)
            ExportFormat.JSON -> buildJson(items)
        }
        outputStream.use { it.write(content.toByteArray(Charsets.UTF_8)) }
    }
}
