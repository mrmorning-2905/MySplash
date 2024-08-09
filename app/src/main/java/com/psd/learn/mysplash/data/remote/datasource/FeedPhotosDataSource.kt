package com.psd.learn.mysplash.data.remote.datasource

import android.util.Log
import com.psd.learn.mysplash.SortByType
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.local.entity.toPhotoItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import com.psd.learn.mysplash.runSuspendCatching
import kotlinx.coroutines.CoroutineDispatcher

class FeedPhotosDataSource(
    private val unSplashApiService: UnSplashApiService,
    private val coroutineDispatcher: CoroutineDispatcher,
    query: String?
) : AbsPagingDataSource<PhotoItem>(query) {

    override suspend fun getResultPagingData(
        query: String?,
        page: Int,
        perPage: Int
    ): Result<List<PhotoItem>> {
        return runSuspendCatching(coroutineDispatcher) {
            val result = unSplashApiService.getPhotoListOnFeed(page, perPage, query ?: SortByType.LATEST_TYPE)
            Log.d("sangpd", "getResultPagingData_query: $query - result: ${result.size}")
            result.map { it.toPhotoItem() }
        }
    }
}