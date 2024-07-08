package com.psd.learn.mysplash.data.remote.repository

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
import kotlinx.coroutines.flow.Flow

class UnSplashPagingRepository(
    private val unSplashApiService: UnSplashApiService
) {
    private val TAG = UnSplashPagingRepository::class.java.simpleName

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
            pagingSourceFactory = { FeedPhotosDataSource(unSplashApiService) }
        ).flow
    }

    fun getFeedCollectionsStream(): Flow<PagingData<CollectionItem>> {
        return Pager(
            config = pageConfig,
            pagingSourceFactory = { FeedCollectionsDataSource(unSplashApiService) }
        ).flow
    }

    fun getCollectionPhotosStream(query: String?): Flow<PagingData<PhotoItem>> {
        return Pager(
            config = pageConfig,
            pagingSourceFactory = { CollectionDetailsDataSource(unSplashApiService, query) }
        ).flow
    }
}