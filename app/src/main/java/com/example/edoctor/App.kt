package com.example.edoctor

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val DARK_THEME = true
const val DARK_THEME_KEY = "dark_theme_key"
class App : Application() {
    private lateinit var sharePref: SharedPreferences
    var darkTheme = false // Убрали private, чтобы можно было читать из других классов

    override fun onCreate() {
        super.onCreate()
        sharePref = getSharedPreferences("DARK_THEME", MODE_PRIVATE)
        darkTheme = sharePref.getBoolean(DARK_THEME_KEY, false) // Используем Boolean вместо String
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        sharePref.edit().putBoolean(DARK_THEME_KEY, darkThemeEnabled).apply()

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}