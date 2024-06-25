package com.psd.learn.mysplash.data.remote.datasource

import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.local.entity.toPhotoItem
import com.psd.learn.mysplash.data.remote.repository.UnSplashApiService
import com.psd.learn.mysplash.utils.log.Logger
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
        Logger.d(TAG, "getPhoto() - response: $response")
        return response.toPhotoItem()
    }
}