package com.psd.learn.mysplash.utils.log

import android.util.Log

class LogcatLogWriter : LogWriter {
    private val MY_SPLASH_APP_TAG = "MySplash_"
    override fun v(msg: String) {
        Log.v(MY_SPLASH_APP_TAG, msg)
    }

    override fun d(msg: String) {
        Log.d(MY_SPLASH_APP_TAG, msg)
    }

    override fun i(msg: String) {
        Log.i(MY_SPLASH_APP_TAG, msg)
    }

    override fun w(msg: String) {
        Log.w(MY_SPLASH_APP_TAG, msg)
    }

    override fun e(msg: String) {
        Log.e(MY_SPLASH_APP_TAG, msg)
    }
}