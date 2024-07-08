package com.psd.learn.mysplash.data.remote.datasource

import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.local.entity.toPhotoItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PhotoDetailsDataSource @Inject constructor(
    private val unSplashApiService: UnSplashApiService
) {

    private val TAG = PhotoDetailsDataSource::class.java.simpleName

    suspend fun getPhoto(photoId: String): PhotoItem {
        val response = withContext(Dispatchers.IO) {
            unSplashApiService.getPhotoItem(photoId)
        }
        return response.toPhotoItem()
    }
}