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

    fun registerPreferenceChangedListener(context: Context, listener: OnSharedPreferenceChangeListener) {
        getDefaultSharedPreferences(context).registerOnSharedPreferenceChangeListener(listener)
    }

    fun removePreferenceChangedListener(context: Context, listener: OnSharedPreferenceChangeListener) {
        getDefaultSharedPreferences(context).unregisterOnSharedPreferenceChangeListener(listener)
    }
}