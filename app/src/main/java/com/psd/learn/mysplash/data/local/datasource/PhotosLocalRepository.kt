package com.psd.learn.mysplash.data.local.datasource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.psd.learn.mysplash.PAGING_SIZE
import com.psd.learn.mysplash.data.local.dao.PhotosDao
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.utils.log.Logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PhotosLocalRepository (
    private val photosDao: PhotosDao,
    private val dispatcher: CoroutineDispatcher
) {

    fun getFavoritePhotosStream(): Flow<PagingData<PhotoItem>> {
        return Pager(
            config = PagingConfig(pageSize = PAGING_SIZE, enablePlaceholders = false),
            pagingSourceFactory = {PhotosLocalPagingSource(photosDao)}
        ).flow
    }

    suspend fun getPhotoList(): List<PhotoItem> {
        val result = withContext(dispatcher) {
            try {
                photosDao.getAllPhotos()
            } catch (e: Exception) {
                Logger.d("sangpd", "getPhotoList - exception: $e")
                emptyList()
            }
        }
        return result
    }

    suspend fun addFavoritePhoto(photoItem: PhotoItem) {
        withContext(dispatcher) {
            photosDao.insertPhoto(photoItem)
        }
    }

    suspend fun removeFavoritePhoto(photoItem: PhotoItem) {
        withContext(dispatcher) {
            photosDao.deletePhoto(photoItem)
        }
    }
}