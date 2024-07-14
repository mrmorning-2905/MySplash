package com.psd.learn.mysplash.data.remote.datasource

import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.data.local.entity.toCollectionItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import com.psd.learn.mysplash.runSuspendCatching
import kotlinx.coroutines.CoroutineDispatcher

class UserDetailsCollectionsDataSource(
    private val apiService: UnSplashApiService,
    private val coroutineDispatcher: CoroutineDispatcher,
    userNameAccount: String
) : AbsPagingDataSource<CollectionItem>(userNameAccount) {

    override suspend fun getResultPagingData(
        query: String?,
        page: Int,
        perPage: Int
    ): Result<List<CollectionItem>> {
        if (query == null) return Result.failure(Exception("query is null"))
        return runSuspendCatching(coroutineDispatcher) {
            apiService.getUserCollections(
                userNameAccount = query,
                page = page,
                perPage = perPage
            ).map { it.toCollectionItem() }
        }
    }
}