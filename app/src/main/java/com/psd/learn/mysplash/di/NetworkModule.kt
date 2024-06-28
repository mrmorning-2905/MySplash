package com.psd.learn.mysplash.di

import com.psd.learn.mysplash.BuildConfig
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import com.psd.learn.mysplash.data.remote.repository.UnSplashPagingRepository
import com.psd.learn.mysplash.network.AuthorizationInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MySplashClientIdQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MySplashBaseUrlQualifier

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {
    companion object {
        @Provides
        @MySplashBaseUrlQualifier
        fun provideMySplashBaseUrl(): String = BuildConfig.UNSPLASH_BASE_URL

        @Provides
        @MySplashClientIdQualifier
        fun provideMySplashClientId(): String = BuildConfig.UNSPLASH_CLIENT_ID

        @Provides
        @Singleton
        fun moshi(): Moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        @Provides
        fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        @Provides
        @Singleton
        fun okHttpClient(
            httpLoggingInterceptor: HttpLoggingInterceptor,
            authorizationInterceptor: AuthorizationInterceptor
        ): OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addNetworkInterceptor(httpLoggingInterceptor)
            .addInterceptor(authorizationInterceptor)
            .build()

        @Provides
        @Singleton
        fun retrofit(
            okHttpClient: OkHttpClient,
            moshi: Moshi,
            @MySplashBaseUrlQualifier mySplashBaseUrl: String
        ): Retrofit = Retrofit.Builder()
            .baseUrl(mySplashBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()


        @Provides
        @Singleton
        fun provideMySplashService(
            retrofit: Retrofit
        ): UnSplashApiService = UnSplashApiService(retrofit)

        @Provides
        @Singleton
        fun providePagingRepository(unSplashApiService: UnSplashApiService) : UnSplashPagingRepository {
            return UnSplashPagingRepository(unSplashApiService)
        }
    }
}