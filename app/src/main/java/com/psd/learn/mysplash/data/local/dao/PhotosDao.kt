package com.psd.learn.mysplash.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotosDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photoItem: PhotoItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(photoList: List<PhotoItem>)

    @Delete
    suspend fun deletePhoto(photoItem: PhotoItem)

    @Query("DELETE FROM PHOTO_TABLE WHERE photo_id =:photoId")
    suspend fun deletePhotoById(photoId: String)

    @Query("DELETE FROM PHOTO_TABLE")
    suspend fun deleteAllPhotos()

    @Query("SELECT * FROM PHOTO_TABLE WHERE photo_id =:photoId")
    suspend fun getPhotoById(photoId: String): PhotoItem?

    @Query("SELECT photo_id FROM PHOTO_TABLE WHERE is_favorite = TRUE")
    fun getAllFavoritePhotoIds(): Flow<List<String>>

    @Query("SELECT photo_id FROM PHOTO_TABLE WHERE photo_id = :id AND is_favorite = TRUE")
    fun observerFavoritePhotoIdFlow(id: String): Flow<String?>

    @Query("SELECT * FROM PHOTO_TABLE WHERE is_favorite = TRUE")
    fun getAllFavoritePhotosPagingSource(): PagingSource<Int, PhotoItem>

    @Query("SELECT * FROM PHOTO_TABLE WHERE is_wallpaper = TRUE")
    fun getAllWallpaperPhotosPagingSource(): PagingSource<Int, PhotoItem>

    @Query("DELETE FROM PHOTO_TABLE WHERE is_favorite = TRUE")
    suspend fun deleteAllFavoritePhotos()

    @Query("DELETE FROM PHOTO_TABLE WHERE is_wallpaper = TRUE")
    suspend fun deleteAllWallpaperPhotos()
}