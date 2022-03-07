package com.example.nasa.utils

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.nasa.R
import com.example.nasa.ui.settings.KEY_CURRENT_DAY_NIGHT_MODE
import com.example.nasa.ui.settings.KEY_CURRENT_THEME
import com.example.nasa.ui.settings.KEY_SETTINGS_LOCAL


enum class Theme(val value: Int) {
    Base(0), Red(1), Blue(2), Green(3);

    companion object {
        fun fromInt(value: Int) = Theme.values().first { it.value == value }
    }
}

enum class DayNightMode(val value: Int) {
    Base(0), Day(1), Night(2);

    companion object {
        fun fromInt(value: Int) = DayNightMode.values().first { it.value == value }
    }
}

fun getCurrentTheme(activity: Activity): Theme {
    val sharedPreferences = activity
        .getSharedPreferences(KEY_SETTINGS_LOCAL, AppCompatActivity.MODE_PRIVATE)
    return Theme.fromInt(sharedPreferences.getInt(KEY_CURRENT_THEME, Theme.Base.value))
}

fun getRealStyle(currentTheme: Theme): Int {
    return when (currentTheme) {
        Theme.Red -> R.style.RedNasaTheme
        Theme.Blue -> R.style.BlueNasaTheme
        Theme.Green -> R.style.GreenNasaTheme
        else -> R.style.BaseNasaTheme
    }
}

fun getCurrentDayNightMode(activity: Activity): DayNightMode {
    val sharedPreferences = activity
        .getSharedPreferences(KEY_SETTINGS_LOCAL, AppCompatActivity.MODE_PRIVATE)
    return DayNightMode
        .fromInt(sharedPreferences.getInt(KEY_CURRENT_DAY_NIGHT_MODE, DayNightMode.Base.value))
}


fun getDayNightMode(mode: DayNightMode): Int {
    return when (mode) {
        DayNightMode.Day -> AppCompatDelegate.MODE_NIGHT_NO
        DayNightMode.Night -> AppCompatDelegate.MODE_NIGHT_YES
        else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }
}