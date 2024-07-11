package com.psd.learn.mysplash.ui.utils

import android.os.SystemClock
import android.util.Log

private var lastClickTime: Long = 0
private var prevId = Int.MIN_VALUE
private var prevPosition = Int.MIN_VALUE
private const val DOUBLE_CLICK_TIME = 400L
fun isValidClick(id: Int, position: Int = -1, delay: Long = DOUBLE_CLICK_TIME): Boolean {
    val clickTime = SystemClock.elapsedRealtime()
    if (prevId == id && prevPosition == position) {
        if (clickTime - lastClickTime < delay) {
            Log.d("sangpd", "invalid click - ${clickTime - lastClickTime}")
            return false
        }
    } else {
        Log.d("sangpd", "isValidClick()] View is different")
    }

    lastClickTime = clickTime
    prevId = id
    prevPosition = position
    return true
}