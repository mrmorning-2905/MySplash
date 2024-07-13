package com.psd.learn.mysplash.data.remote.datasource

import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.data.local.entity.toCollectionItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class FeedCollectionsDataSource(
    private val unSplashApiService: UnSplashApiService,
    private val coroutineDispatcher: CoroutineDispatcher
) : AbsPagingDataSource<CollectionItem>() {

    override val TAG: String
        get() = FeedCollectionsDataSource::class.java.simpleName

    override suspend fun getListDataPaging(
        query: String?,
        page: Int,
        perPage: Int
    ): List<CollectionItem> {

        return withContext(coroutineDispatcher) {
            unSplashApiService.getCollectionListOnFeed(page, perPage)
                .map { it.toCollectionItem() }
        }
    }
}