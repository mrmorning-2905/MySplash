package com.psd.learn.mysplash.data.remote.datasource

import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.local.entity.toPhotoItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import com.psd.learn.mysplash.runSuspendCatching
import kotlinx.coroutines.CoroutineDispatcher

class TopicDetailsDataSource(
    private val unSplashApiService: UnSplashApiService,
    private val coroutineDispatcher: CoroutineDispatcher,
    query: String?
) : AbsPagingDataSource<PhotoItem>(query) {

    override suspend fun getResultPagingData(
        query: String?,
        page: Int,
        perPage: Int
    ): Result<List<PhotoItem>> {
        if (query == null) return Result.failure(Exception("query is null"))
        return runSuspendCatching(coroutineDispatcher) {
            unSplashApiService.getPhotoListOfTopic(
                id = query,
                page = page,
                perPage = perPage
            ).map { it.toPhotoItem() }
        }
    }
}