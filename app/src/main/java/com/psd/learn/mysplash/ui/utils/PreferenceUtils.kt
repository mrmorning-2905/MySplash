package com.psd.learn.mysplash.ui.utils

import android.content.Context
import androidx.preference.PreferenceManager.getDefaultSharedPreferences

object PreferenceUtils {

    fun getSortByType(context: Context, key: String): Int {
        return getDefaultSharedPreferences(context).getInt(key, -1)
    }

    fun setSortByType(context: Context, key: String, value: Int) {
        getDefaultSharedPreferences(context).edit().apply {
            putInt(key, value)
            apply()
        }
    }

    fun getCurrentSortType()
}