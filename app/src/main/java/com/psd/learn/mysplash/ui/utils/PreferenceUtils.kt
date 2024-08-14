package com.psd.learn.mysplash.ui.utils

import android.content.Context
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.psd.learn.mysplash.SortByType

object PreferenceUtils {
    fun getSortByType(context: Context, key: String): String? {
        return getDefaultSharedPreferences(context).getString(key, SortByType.LATEST_TYPE)
    }

    fun setSortByType(context: Context, key: String, value: String) {
        getDefaultSharedPreferences(context).edit().apply {
            putString(key, value)
            apply()
        }
    }

    fun getAutoWallpaperStatus(context: Context, key: String): Int {
        return getDefaultSharedPreferences(context).getInt(key, 0)
    }

    fun setAutoWallpaperStatus(context: Context, key: String, value: Int) {
        getDefaultSharedPreferences(context).edit().apply {
            putInt(key, value)
            apply()
        }
    }

    fun registerPreferenceChangedListener(context: Context, listener: OnSharedPreferenceChangeListener) {
        getDefaultSharedPreferences(context).registerOnSharedPreferenceChangeListener(listener)
    }

    fun removePreferenceChangedListener(context: Context, listener: OnSharedPreferenceChangeListener) {
        getDefaultSharedPreferences(context).unregisterOnSharedPreferenceChangeListener(listener)
    }
}