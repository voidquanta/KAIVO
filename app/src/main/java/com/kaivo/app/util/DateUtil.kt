package com.kaivo.app.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtil {

    private val displayFormat = SimpleDateFormat("yyyy-MM-dd  HH:mm", Locale.US)
    private val exportFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)

    fun formatForDisplay(timestampMillis: Long): String =
        displayFormat.format(Date(timestampMillis))

    fun formatForExport(timestampMillis: Long): String =
        exportFormat.format(Date(timestampMillis))
}
