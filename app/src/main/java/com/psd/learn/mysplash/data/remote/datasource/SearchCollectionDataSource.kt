package com.psd.learn.mysplash.data.remote.datasource

import com.psd.learn.mysplash.data.local.entity.toCollectionItem
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import com.psd.learn.mysplash.runSuspendCatching
import kotlinx.coroutines.CoroutineDispatcher

class SearchCollectionDataSource(
    private val unSplashApiService: UnSplashApiService,
    private val coroutineDispatcher: CoroutineDispatcher,
    queryText: String?,
    override val totalResult: (Int) -> Unit
) : AbsPagingDataSource<CollectionItem>(queryText) {
    override suspend fun getResultPagingData(
        query: String?,
        page: Int,
        perPage: Int
    ): Result<List<CollectionItem>> {
        if (query == null) return Result.failure(Exception("query is null"))
        return runSuspendCatching(coroutineDispatcher) {
            val response = unSplashApiService.getSearchCollectionResult(
                query = query,
                page = page,
                perPage = perPage
            )
            totalResult(response.total)
            response.results.map { it.toCollectionItem() }
        }
    }
}