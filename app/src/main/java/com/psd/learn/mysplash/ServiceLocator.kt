package com.psd.learn.mysplash

import androidx.annotation.MainThread
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import com.psd.learn.mysplash.network.AuthorizationInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ServiceLocator {
    private const val UNSPLASH_BASE_URL = "https://api.unsplash.com/"


    @set:MainThread
    @get:MainThread
    private var _application: MySplashApplication? = null

    @MainThread
    fun initWith(application: MySplashApplication) {
        _application = application
    }

    @get:MainThread
    val myApplication: MySplashApplication
        get() = checkNotNull(_application) {
            "ServiceLocator must be initialized. " +
                    "Call ServiceLocator.initWith(this) in your Application class."
        }


    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    private val httpLoggingInterceptor
        get() = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    private val authorizationInterceptor: AuthorizationInterceptor
        get() = AuthorizationInterceptor(clientId = BuildConfig.UNSPLASH_CLIENT_ID)

    val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addNetworkInterceptor(httpLoggingInterceptor)
            .addInterceptor(authorizationInterceptor)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(UNSPLASH_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    val unSplashService: UnSplashApiService by lazy { UnSplashApiService(retrofit) }
}