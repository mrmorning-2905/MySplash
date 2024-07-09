package com.psd.learn.mysplash.data.local.datasource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.psd.learn.mysplash.PAGING_SIZE
import com.psd.learn.mysplash.data.local.dao.PhotosDao
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class PhotosLocalRepository(
    private val photosDao: PhotosDao,
    private val dispatcher: CoroutineDispatcher
) {

    fun getFavoritePhotosStream(): Flow<PagingData<PhotoItem>> {
        return Pager(
            config = PagingConfig(pageSize = PAGING_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { photosDao.getAllPhotosPagingSource() }
        ).flow
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

    suspend fun checkFavoritePhotoById(photoId: String): Boolean {
        val photoItem = withContext(dispatcher) {
            photosDao.getPhotoById(photoId)
        }
        return photoItem != null
    }

    fun getPhotoIdsStream(): Flow<List<String>> = photosDao
        .getAllPhotoIds()
        .flowOn(dispatcher)

    fun observerPhotoId(id: String): Flow<String?> = photosDao
        .getPhotoIdFlow(id)
        .flowOn(dispatcher)
}