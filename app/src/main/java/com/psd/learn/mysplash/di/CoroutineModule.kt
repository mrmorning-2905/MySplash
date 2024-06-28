package com.psd.learn.mysplash.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MainDispatcher

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultDispatcher

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IoDispatcher

@Module
@InstallIn(SingletonComponent::class)
interface CoroutineModule {
    companion object {
        @Provides
        @MainDispatcher
        fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

        @Provides
        @IoDispatcher
        fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

        @Provides
        @DefaultDispatcher
        fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
    }
}