package com.psd.learn.mysplash

import android.app.Application

class MySplashApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ServiceLocator.initWith(this)
    }
}