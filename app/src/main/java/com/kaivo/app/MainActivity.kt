package com.kaivo.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import com.kaivo.app.ui.KaivoApp

class MainActivity : ComponentActivity() {

    // Ensures the per-app language chosen in Settings (via
    // AppCompatDelegate.setApplicationLocales) is applied to this
    // Activity's Context on every level of Android, not just 13+.
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(AppCompatDelegate.attachBaseContext(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as KaivoApplication

        setContent {
            KaivoApp(app = app)
        }
    }
}
