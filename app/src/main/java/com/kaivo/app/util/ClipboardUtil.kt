package com.kaivo.app.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

/**
 * Thin wrapper around the system clipboard. Every function here is only
 * ever called directly from a user tap (Paste button / Copy button) —
 * KAIVO never reads the clipboard proactively, on a timer, or from a
 * background service.
 */
object ClipboardUtil {

    fun readClipboard(context: Context): String? {
        val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            ?: return null
        if (!manager.hasPrimaryClip()) return null
        val clip = manager.primaryClip ?: return null
        if (clip.itemCount == 0) return null
        val text = clip.getItemAt(0)?.coerceToText(context)?.toString()
        return text?.takeIf { it.isNotBlank() }
    }

    fun writeClipboard(context: Context, text: String) {
        val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            ?: return
        val clip = ClipData.newPlainText("KAIVO", text)
        manager.setPrimaryClip(clip)
    }
}
