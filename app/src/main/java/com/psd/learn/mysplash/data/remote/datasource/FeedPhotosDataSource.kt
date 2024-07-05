package com.psd.learn.mysplash.data.remote.datasource

import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.local.entity.toPhotoItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FeedPhotosDataSource(
    private val unSplashApiService: UnSplashApiService,
) : AbsPagingDataSource<PhotoItem>() {

    override val TAG: String
        get() = FeedPhotosDataSource::class.java.simpleName

    override suspend fun getListDataPaging(
        query: String?,
        page: Int,
        perPage: Int
    ): List<PhotoItem> {
        val response = withContext(Dispatchers.IO) {
            unSplashApiService.getPhotoListOnFeed(page, perPage)
        }
        return response.map { it.toPhotoItem() }
    }
}