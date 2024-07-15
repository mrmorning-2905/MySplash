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
            pagingSourceFactory = { photosDao.getAllPhotosPagingSource() }
        ).flow
    }

    suspend fun addFavoritePhoto(photoItem: PhotoItem): Result<Unit> =
        runSuspendCatching(dispatcher) {
            photosDao.insertPhoto(photoItem)
        }

    suspend fun removeFavoritePhoto(photoItem: PhotoItem): Result<Unit> =
        runSuspendCatching(dispatcher) {
            photosDao.deletePhoto(photoItem)
        }

    suspend fun checkFavoritePhotoById(photoId: String): Result<Boolean> =
        runSuspendCatching(dispatcher) {
            photosDao.getPhotoById(photoId) != null
        }

    fun observerLocalPhotoIdsStream(): Flow<Result<List<String>>> = photosDao
        .getAllPhotoIds()
        .flowOn(dispatcher)
        .mapToResult()

    fun observerPhotoId(id: String): Flow<Result<String?>> = photosDao
        .getPhotoIdFlow(id)
        .flowOn(dispatcher)
        .mapToResult()

//    fun observerPhotoId(id: String): Flow<String?> = photosDao
//        .getPhotoIdFlow(id)
//        .flowOn(dispatcher)
}