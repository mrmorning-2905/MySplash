package com.psd.learn.mysplash.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.psd.learn.mysplash.data.remote.datasource.SearchDataSourceFactory
import com.psd.learn.mysplash.NETWORK_PAGE_SIZE
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.remote.datasource.FeedCollectionsDataSource
import com.psd.learn.mysplash.data.remote.datasource.FeedPhotosDataSource
import com.psd.learn.mysplash.utils.log.Logger
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UnSplashPagingRepository @Inject constructor(
    private val unSplashApiService: UnSplashApiService
) {
    private val TAG = UnSplashPagingRepository::class.java.simpleName

    private val pageConfig = PagingConfig(
        pageSize = NETWORK_PAGE_SIZE,
        enablePlaceholders = false
    )

    fun <T : Any> getSearchResultStream(query: String?, searchType: Int): Flow<PagingData<T>> {
        Logger.d(TAG, "getSearchResultStream() - query: $query - searchType: $searchType")
        return Pager(
            config = pageConfig,
            pagingSourceFactory = {
                SearchDataSourceFactory.getSearchDataSource<T>(
                    unSplashApiService,
                    searchType,
                    query
                )
            }
        ).flow
    }

    fun getFeedPhotosStream(): Flow<PagingData<PhotoItem>> {
        return Pager(
            config = pageConfig,
            pagingSourceFactory = { FeedPhotosDataSource(unSplashApiService) }
        ).flow
    }

    fun getFeedCollectionsStream(): Flow<PagingData<CollectionItem>> {
        return Pager(
            config = pageConfig,
            pagingSourceFactory = { FeedCollectionsDataSource(unSplashApiService) }
        ).flow
    }
}