package com.psd.learn.mysplash.data.remote.datasource

import com.psd.learn.mysplash.SEARCH_COLLECTIONS_TYPE
import com.psd.learn.mysplash.SEARCH_PHOTOS_TYPE
import com.psd.learn.mysplash.SEARCH_USERS_TYPE
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import kotlinx.coroutines.CoroutineDispatcher

object SearchDataSourceFactory {
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getSearchDataSource(
        unSplashApiService: UnSplashApiService,
        coroutineDispatcher: CoroutineDispatcher,
        searchType: Int,
        queryText: String?,
        onTotalResult: (Int) -> Unit
    ): AbsPagingDataSource<T> {
        return when (searchType) {
            SEARCH_PHOTOS_TYPE -> SearchPhotoDataSource(unSplashApiService, coroutineDispatcher, queryText, onTotalResult)
            SEARCH_COLLECTIONS_TYPE -> SearchCollectionDataSource(unSplashApiService,  coroutineDispatcher, queryText, onTotalResult)
            SEARCH_USERS_TYPE -> SearchUserDataSource(unSplashApiService,  coroutineDispatcher, queryText, onTotalResult)
            else -> error("invalid source")
        } as AbsPagingDataSource<T>
    }
}