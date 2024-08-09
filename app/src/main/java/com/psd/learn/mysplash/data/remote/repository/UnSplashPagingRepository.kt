package com.psd.learn.mysplash.data.remote.repository

import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.psd.learn.mysplash.PAGING_SIZE
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.remote.datasource.CollectionDetailsDataSource
import com.psd.learn.mysplash.data.remote.datasource.FeedCollectionsDataSource
import com.psd.learn.mysplash.data.remote.datasource.FeedPhotosDataSource
import com.psd.learn.mysplash.data.remote.datasource.SearchDataSourceFactory
import com.psd.learn.mysplash.data.remote.datasource.UserDetailsPagingSourceFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class UnSplashPagingRepository(
    private val unSplashApiService: UnSplashApiService,
    private val coroutineDispatcher: CoroutineDispatcher
) {

    private val pageConfig = PagingConfig(
        pageSize = PAGING_SIZE,
        enablePlaceholders = false
    )

    fun <T : Any> getSearchResultStream(
        query: String?,
        searchType: Int,
        onTotalResult: (Int) -> Unit
    ): Flow<PagingData<T>> {
        val invalidPagingSource = InvalidatingPagingSourceFactory {
            SearchDataSourceFactory.getSearchDataSource<T>(
                unSplashApiService,
                coroutineDispatcher,
                searchType,
                query
            ) { total -> onTotalResult(total) }
        }
        return Pager(
            config = pageConfig,
            pagingSourceFactory =  invalidPagingSource
        ).flow
    }

    fun getFeedPhotosStream(query: String?): Flow<PagingData<PhotoItem>> {
        return Pager(
            config = pageConfig,
            pagingSourceFactory = { FeedPhotosDataSource(unSplashApiService, coroutineDispatcher, query) }
        ).flow
    }

    fun getFeedCollectionsStream(): Flow<PagingData<CollectionItem>> {
        return Pager(
            config = pageConfig,
            pagingSourceFactory = { FeedCollectionsDataSource(unSplashApiService, coroutineDispatcher) }
        ).flow
    }

    fun getCollectionPhotosStream(query: String?): Flow<PagingData<PhotoItem>> {
        return Pager(
            config = pageConfig,
            pagingSourceFactory = { CollectionDetailsDataSource(unSplashApiService, coroutineDispatcher, query) }
        ).flow
    }

    fun <T : Any> getUserDetailsPagingDataStream(
        userName: String,
        detailType: Int,
    ): Flow<PagingData<T>> {
        val invalidPagingSource = InvalidatingPagingSourceFactory {
            UserDetailsPagingSourceFactory.getUserDetailsPagingDataSource<T>(
                unSplashApiService,
                coroutineDispatcher,
                detailType,
                userName
            )
        }
        return Pager(
            config = pageConfig,
            pagingSourceFactory = invalidPagingSource
        ).flow
    }
}