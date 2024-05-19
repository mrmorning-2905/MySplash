package com.psd.learn.mysplash

import android.app.Application
import com.psd.learn.mysplash.utils.log.LogcatLogWriter
import com.psd.learn.mysplash.utils.log.Logger

class MySplashApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ServiceLocator.initWith(this)
        Logger.init(LogcatLogWriter())
    }
}