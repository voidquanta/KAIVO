package com.kaivo.app

import android.app.Application
import com.kaivo.app.data.ClipboardRepository
import com.kaivo.app.data.KaivoDatabase
import com.kaivo.app.data.SettingsDataStore

class KaivoApplication : Application() {

    lateinit var repository: ClipboardRepository
        private set

    lateinit var settingsDataStore: SettingsDataStore
        private set

    override fun onCreate() {
        super.onCreate()
        val db = KaivoDatabase.getInstance(this)
        repository = ClipboardRepository(db.clipItemDao())
        settingsDataStore = SettingsDataStore(this)
    }
}
