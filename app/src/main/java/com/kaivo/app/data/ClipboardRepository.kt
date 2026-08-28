package com.kaivo.app.data

import kotlinx.coroutines.flow.Flow

class ClipboardRepository(private val dao: ClipItemDao) {

    fun observeAll(): Flow<List<ClipItem>> = dao.observeAll()

    suspend fun getAllOnce(): List<ClipItem> = dao.getAllOnce()

    suspend fun save(content: String) {
        val trimmed = content.trim()
        if (trimmed.isEmpty()) return
        dao.insert(ClipItem(content = trimmed, createdAt = System.currentTimeMillis()))
    }

    suspend fun delete(item: ClipItem) = dao.delete(item)

    suspend fun deleteAll() = dao.deleteAll()

    suspend fun togglePin(item: ClipItem) = dao.setPinned(item.id, !item.isPinned)
}
