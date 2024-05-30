package com.psd.learn.mysplash.network

import com.psd.learn.mysplash.MySplashClientIdQualifier
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthorizationInterceptor @Inject constructor(
    @MySplashClientIdQualifier
    private val clientId: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain
        .request()
        .newBuilder()
        .addHeader("Authorization", "Client-ID $clientId")
        .build()
        .let(chain::proceed)
}