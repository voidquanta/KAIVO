package com.kaivo.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A single clipboard entry the user explicitly chose to save.
 * KAIVO never writes to this table automatically — only in response
 * to the user tapping "Save".
 */
@Entity(tableName = "clip_items")
data class ClipItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val content: String,
    val createdAt: Long,
    val isPinned: Boolean = false
)
