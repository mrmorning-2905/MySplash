package com.psd.learn.mysplash.data.remote.datasource

import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.data.local.entity.toCollectionItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import com.psd.learn.mysplash.runSuspendCatching
import kotlinx.coroutines.CoroutineDispatcher

class FeedCollectionsDataSource(
    private val unSplashApiService: UnSplashApiService,
    private val coroutineDispatcher: CoroutineDispatcher
) : AbsPagingDataSource<CollectionItem>() {

    override suspend fun getResultPagingData(
        query: String?,
        page: Int,
        perPage: Int
    ): Result<List<CollectionItem>> {
        return runSuspendCatching(coroutineDispatcher) {
            unSplashApiService.getCollectionListOnFeed(
                page = page,
                perPage = perPage
            ).map { it.toCollectionItem() }
        }
    }
}