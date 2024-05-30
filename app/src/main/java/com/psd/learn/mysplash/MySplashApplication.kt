package com.psd.learn.mysplash

import android.app.Application
import com.psd.learn.mysplash.utils.log.LogcatLogWriter
import com.psd.learn.mysplash.utils.log.Logger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MySplashApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Logger.init(LogcatLogWriter())
    }
}