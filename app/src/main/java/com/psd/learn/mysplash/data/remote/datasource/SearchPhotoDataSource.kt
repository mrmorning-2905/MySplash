package com.psd.learn.mysplash.data.remote.datasource

import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.local.entity.toPhotoItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import com.psd.learn.mysplash.runSuspendCatching
import kotlinx.coroutines.CoroutineDispatcher

class SearchPhotoDataSource(
    private val unSplashApiService: UnSplashApiService,
    private val coroutineDispatcher: CoroutineDispatcher,
    queryText: String?,
    override val totalResult: (Int) -> Unit
) : AbsPagingDataSource<PhotoItem>(queryText) {

    override suspend fun getResultPagingData(
        query: String?,
        page: Int,
        perPage: Int
    ): Result<List<PhotoItem>> {
        if (query == null) return Result.failure(Exception("query is null"))
        return runSuspendCatching(coroutineDispatcher) {
            val response = unSplashApiService.getSearchPhotoResult(
                query = query,
                page = page,
                perPage = perPage
            )
            totalResult(response.total)
            response.results.map { it.toPhotoItem() }
        }
    }
}