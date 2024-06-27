package com.psd.learn.mysplash.data.local.datasource

import com.psd.learn.mysplash.data.local.dao.PhotosDao
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class PhotoLocalDataSource(
    private val photoDao: PhotosDao,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun getPhotoList(): List<PhotoItem> = withContext(dispatcher) {
        return@withContext try {
            photoDao.getAllPhotos()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addFavoritePhoto(photoItem: PhotoItem) {
        withContext(dispatcher) {
            photoDao.insertPhoto(photoItem)
        }
    }

    suspend fun removeFavoritePhoto(photoItem: PhotoItem) {
        withContext(dispatcher) {
            photoDao.deletePhoto(photoItem)
        }
    }
}