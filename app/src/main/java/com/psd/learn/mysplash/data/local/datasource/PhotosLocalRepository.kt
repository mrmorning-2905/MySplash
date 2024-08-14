package com.psd.learn.mysplash.data.local.datasource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.psd.learn.mysplash.PAGING_SIZE
import com.psd.learn.mysplash.data.local.dao.PhotosDao
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.mapToResult
import com.psd.learn.mysplash.runSuspendCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class PhotosLocalRepository(
    private val photosDao: PhotosDao,
    private val dispatcher: CoroutineDispatcher
) {

    fun getFavoritePhotosStream(): Flow<PagingData<PhotoItem>> {
        return Pager(
            config = PagingConfig(pageSize = PAGING_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { photosDao.getAllFavoritePhotosPagingSource() }
        ).flow
    }

    fun getWallpaperHistoryPhotosStream(): Flow<PagingData<PhotoItem>> {
        return Pager(
            config = PagingConfig(pageSize = PAGING_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { photosDao.getAllWallpaperPhotosPagingSource() }
        ).flow
    }

    suspend fun addFavoritePhoto(photoItem: PhotoItem): Result<Unit> =
        runSuspendCatching(dispatcher) {
            photosDao.insertPhoto(photoItem)
        }

    suspend fun addWallpaperPhotoToHistory(photoItem: PhotoItem): Result<Unit> =
        runSuspendCatching(dispatcher) {
            photosDao.insertPhoto(photoItem)
        }

    suspend fun addListFavorite(photoList: List<PhotoItem>): Result<Unit> =
        runSuspendCatching(dispatcher) {
            photosDao.insertPhotos(photoList)
        }

    suspend fun removeFavoritePhoto(photoItem: PhotoItem): Result<Unit> =
        runSuspendCatching(dispatcher) {
            photosDao.deletePhoto(photoItem)
        }

    fun observerLocalPhotoIdsStream(): Flow<Result<List<String>>> = photosDao
        .getAllFavoritePhotoIds()
        .flowOn(dispatcher)
        .mapToResult()

    fun observerPhotoId(id: String): Flow<Result<String?>> = photosDao
        .observerFavoritePhotoIdFlow(id)
        .flowOn(dispatcher)
        .mapToResult()
}