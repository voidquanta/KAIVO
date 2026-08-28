package com.kaivo.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ClipItem::class], version = 1, exportSchema = false)
abstract class KaivoDatabase : RoomDatabase() {

    abstract fun clipItemDao(): ClipItemDao

    companion object {
        @Volatile
        private var INSTANCE: KaivoDatabase? = null

        fun getInstance(context: Context): KaivoDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    KaivoDatabase::class.java,
                    "kaivo.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
