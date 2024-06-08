package com.psd.learn.mysplash.data.remote.datasource
import com.psd.learn.mysplash.SEARCH_COLLECTIONS_TYPE
import com.psd.learn.mysplash.SEARCH_PHOTOS_TYPE
import com.psd.learn.mysplash.SEARCH_USERS_TYPE
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService

object SearchDataSourceFactory {
        @Suppress("UNCHECKED_CAST")
        fun <T : Any> getSearchDataSource(unSplashApiService: UnSplashApiService, searchType: Int, queryText: String?): AbsPagingDataSource<T> {
                return when(searchType) {
                        SEARCH_PHOTOS_TYPE -> SearchPhotoDataSource(unSplashApiService, queryText)
                        SEARCH_COLLECTIONS_TYPE -> SearchCollectionDataSource(unSplashApiService, queryText)
                        SEARCH_USERS_TYPE -> SearchUserDataSource(unSplashApiService, queryText)
                        else -> error("invalid source")
                } as AbsPagingDataSource<T>
        }
}