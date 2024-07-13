package com.psd.learn.mysplash.data.remote.datasource

import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.local.entity.toPhotoItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class PhotoDetailsDataSource (
    private val unSplashApiService: UnSplashApiService,
    private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend fun getPhoto(photoId: String): PhotoItem {
        return withContext(coroutineDispatcher) {
            unSplashApiService.getPhotoItem(photoId).toPhotoItem()
        }
    }
}