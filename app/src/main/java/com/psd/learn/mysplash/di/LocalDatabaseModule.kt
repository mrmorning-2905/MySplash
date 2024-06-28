package com.psd.learn.mysplash.di

import android.content.Context
import com.psd.learn.mysplash.data.local.dao.PhotosDao
import com.psd.learn.mysplash.data.local.database.PhotosDatabase
import com.psd.learn.mysplash.data.local.datasource.PhotosLocalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface LocalDatabaseModule {
    companion object {
        @Provides
        @Singleton
        fun providePhotoLocalDatabase(
            @ApplicationContext
            applicationContext: Context
        ): PhotosDatabase = PhotosDatabase.getInstance(applicationContext)

        @Provides
        @Singleton
        fun providePhotoDao(
            photosDatabase: PhotosDatabase
        ): PhotosDao = photosDatabase.photoDao()

        @Provides
        @Singleton
        fun providePhotosLocalPagingRepository(
            photosDao: PhotosDao,
            @IoDispatcher dispatcher: CoroutineDispatcher
        ) : PhotosLocalRepository {
            return PhotosLocalRepository(photosDao, dispatcher)
        }
    }
}