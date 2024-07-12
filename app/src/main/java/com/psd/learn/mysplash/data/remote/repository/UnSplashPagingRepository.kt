package com.psd.learn.mysplash.data.remote.repository

import android.util.Log
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
        val dataSource = SearchDataSourceFactory.getSearchDataSource<T>(
            unSplashApiService,
            coroutineDispatcher,
            searchType,
            query
        ) { total -> onTotalResult(total) }
        return Pager(
            config = pageConfig,
            pagingSourceFactory = { dataSource }
        ).flow
    }

    fun getFeedPhotosStream(): Flow<PagingData<PhotoItem>> {
        return Pager(
            config = pageConfig,
            pagingSourceFactory = { FeedPhotosDataSource(unSplashApiService, coroutineDispatcher) }
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
        Log.d("sangpd", "getUserDetailsPagingDataStream_userName: $userName")
        val dataSource = UserDetailsPagingSourceFactory.getUserDetailsPagingDataSource<T>(
            unSplashApiService,
            coroutineDispatcher,
            detailType,
            userName
        )
        return Pager(
            config = pageConfig,
            pagingSourceFactory = { dataSource }
        ).flow
    }
}