package com.psd.learn.mysplash.data.remote.datasource

import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.local.entity.toPhotoItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import com.psd.learn.mysplash.runSuspendCatching
import kotlinx.coroutines.CoroutineDispatcher

class FeedPhotosDataSource(
    private val unSplashApiService: UnSplashApiService,
    private val coroutineDispatcher: CoroutineDispatcher
) : AbsPagingDataSource<PhotoItem>() {

    override suspend fun getResultPagingData(
        query: String?,
        page: Int,
        perPage: Int
    ): Result<List<PhotoItem>> {
        return runSuspendCatching(coroutineDispatcher) {
            unSplashApiService.getPhotoListOnFeed(
                page, perPage
            ).map { it.toPhotoItem() }
        }
    }
}