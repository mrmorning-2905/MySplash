package com.psd.learn.mysplash.data.remote.datasource

import android.util.Log
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.local.entity.toPhotoItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FeedPhotosDataSource(
    private val unSplashApiService: UnSplashApiService,
    private val coroutineDispatcher: CoroutineDispatcher
) : AbsPagingDataSource<PhotoItem>() {

    override val TAG: String
        get() = FeedPhotosDataSource::class.java.simpleName

    override suspend fun getListDataPaging(
        query: String?,
        page: Int,
        perPage: Int
    ): List<PhotoItem> {
        return withContext(coroutineDispatcher) {
            unSplashApiService.getPhotoListOnFeed(page, perPage).map { it.toPhotoItem() }
        }
    }
}