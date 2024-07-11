package com.psd.learn.mysplash.data.remote.datasource

import com.psd.learn.mysplash.USER_DETAILS_COLLECTIONS_TYPE
import com.psd.learn.mysplash.USER_DETAILS_LIKED_TYPE
import com.psd.learn.mysplash.USER_DETAILS_PHOTOS_TYPE
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import kotlinx.coroutines.CoroutineDispatcher

object UserDetailsPagingSourceFactory {

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getUserDetailsPagingDataSource(
        unSplashApiService: UnSplashApiService,
        coroutineDispatcher: CoroutineDispatcher,
        detailType: Int,
        userName: String,
    ): AbsPagingDataSource<T> {
        return when (detailType) {
            USER_DETAILS_PHOTOS_TYPE -> UserDetailsPhotosDataSource(unSplashApiService, coroutineDispatcher, userName)
            USER_DETAILS_COLLECTIONS_TYPE -> UserDetailsCollectionsDataSource(unSplashApiService,  coroutineDispatcher, userName)
            USER_DETAILS_LIKED_TYPE -> UserDetailsLikedPhotosDataSource(unSplashApiService,  coroutineDispatcher, userName)
            else -> error("invalid source")
        } as AbsPagingDataSource<T>
    }
}