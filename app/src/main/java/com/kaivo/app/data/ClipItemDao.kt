package com.kaivo.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ClipItemDao {

    // Pinned items first, then newest first.
    @Query("SELECT * FROM clip_items ORDER BY isPinned DESC, createdAt DESC")
    fun observeAll(): Flow<List<ClipItem>>

    @Query("SELECT * FROM clip_items ORDER BY isPinned DESC, createdAt DESC")
    suspend fun getAllOnce(): List<ClipItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ClipItem): Long

    @Update
    suspend fun update(item: ClipItem)

    @Delete
    suspend fun delete(item: ClipItem)

    @Query("DELETE FROM clip_items")
    suspend fun deleteAll()

    @Query("UPDATE clip_items SET isPinned = :pinned WHERE id = :id")
    suspend fun setPinned(id: Long, pinned: Boolean)
}
