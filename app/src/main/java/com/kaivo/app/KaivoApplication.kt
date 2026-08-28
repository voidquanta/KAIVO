package com.kaivo.app

import android.app.Application
import com.kaivo.app.data.ClipboardRepository
import com.kaivo.app.data.KaivoDatabase
import com.kaivo.app.data.SettingsDataStore

class KaivoApplication : Application() {
    lateinit var repository: ClipboardRepository
    lateinit var settingsDataStore: SettingsDataStore

    override fun onCreate() {
        super.onCreate()
        val database = KaivoDatabase.getInstance(this)
        repository = ClipboardRepository(database.clipItemDao())
        settingsDataStore = SettingsDataStore(this)
    }
}
