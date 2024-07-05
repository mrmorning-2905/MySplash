package com.psd.learn.mysplash.data.remote.datasource

import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.data.local.entity.toCollectionItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FeedCollectionsDataSource(
    private val unSplashApiService: UnSplashApiService,
) : AbsPagingDataSource<CollectionItem>() {

    override val TAG: String
        get() = FeedCollectionsDataSource::class.java.simpleName

    override suspend fun getListDataPaging(
        query: String?,
        page: Int,
        perPage: Int
    ): List<CollectionItem> {
        val response = withContext(Dispatchers.IO) {
            unSplashApiService.getCollectionListOnFeed(page, perPage)
        }
        return response.map { it.toCollectionItem() }
    }
}