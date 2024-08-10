package com.psd.learn.mysplash.data.remote.datasource

import android.util.Log
import com.psd.learn.mysplash.SortByType
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.data.local.entity.toCollectionItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import com.psd.learn.mysplash.runSuspendCatching
import kotlinx.coroutines.CoroutineDispatcher

class FeedTopicDataSource(
    private val unSplashApiService: UnSplashApiService,
    private val coroutineDispatcher: CoroutineDispatcher,
    query: String?
) : AbsPagingDataSource<CollectionItem>(query) {
    override suspend fun getResultPagingData(
        query: String?,
        page: Int,
        perPage: Int
    ): Result<List<CollectionItem>> {

        return runSuspendCatching(coroutineDispatcher) {
            val result = unSplashApiService.getTopicList(page, perPage, query ?: SortByType.POSITION_TYPE)
            Log.d("sangpd", "getResultPagingData_topicList_query: $query - size: ${result.size}")
            result.map { it.toCollectionItem() }

        }
    }

}