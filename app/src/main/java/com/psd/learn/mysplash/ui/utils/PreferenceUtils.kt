package com.psd.learn.mysplash.ui.utils

import android.content.Context
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
}