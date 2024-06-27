package com.psd.learn.mysplash.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.psd.learn.mysplash.data.local.entity.PhotoItem

@Dao
interface PhotosDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photoItem: PhotoItem)

    @Insert
    suspend fun insertPhotos(photoList: List<PhotoItem>)

    @Delete
    suspend fun deletePhoto(photoItem: PhotoItem)

    @Query("DELETE FROM PHOTO_TABLE WHERE photo_id =:photoId")
    suspend fun deletePhotoById(photoId: String)

    @Query("DELETE FROM PHOTO_TABLE")
    suspend fun deleteAllPhotos()

    @Query("SELECT * FROM PHOTO_TABLE")
    suspend fun getAllPhotos(): List<PhotoItem>

    @Query("SELECT * FROM PHOTO_TABLE WHERE photo_id =:photoId")
    suspend fun getPhotoById(photoId: String): PhotoItem?
}