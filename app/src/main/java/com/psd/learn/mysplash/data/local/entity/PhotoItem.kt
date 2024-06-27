package com.psd.learn.mysplash.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.psd.learn.mysplash.data.remote.entity.PhotoResponseItem

@Entity(tableName = "PHOTO_TABLE")
data class PhotoItem (
    @PrimaryKey
    @ColumnInfo(name = "photo_id")
    val photoId: String,

    @ColumnInfo(name = "user_id")
    val userId: String,

    @ColumnInfo(name = "user_name")
    val userName: String,

    @ColumnInfo(name = "profile_url")
    val userProfileUrl: String,

    @ColumnInfo(name = "cover_url")
    val coverPhotoUrl: String,

    @ColumnInfo(name = "thumbnail_url")
    val coverThumbnailUrl: String,

    @ColumnInfo(name = "cover_color")
    val coverColor: String,

    @ColumnInfo(name = "photo_des")
    val photoDescription: String,

    @ColumnInfo(name = "likes")
    val numberLikes: Int,

    @ColumnInfo(name = "views")
    val numberView: Int,

    @ColumnInfo(name = "downloads")
    val numberDownload: Int,

    @ColumnInfo(name = "width")
    val width: Int,

    @ColumnInfo(name = "height")
    val height: Int,

    //tag
    //val tagList: Set<String> = emptySet(),

    @ColumnInfo(name = "location")
    val location: String,

    //Exif
    @ColumnInfo(name = "camera")
    val cameraName: String,

    @ColumnInfo(name = "focal_length")
    val focalLength: String,

    @ColumnInfo(name = "iso")
    val iso: String,

    @ColumnInfo(name = "aperture")
    val aperture: String,

    @ColumnInfo(name = "exposure_time")
    val exposureTime: String
)

fun PhotoResponseItem.toPhotoItem(): PhotoItem {
    return PhotoItem(
        photoId = id,
        userName = userResponse?.name ?: "Unknown",
        userProfileUrl = userResponse?.profileImage?.medium ?: "",
        coverPhotoUrl = urls.regular,
        photoDescription = altDescription ?: "",
        numberLikes = likes ?: 0,
        numberView = views ?: 0,
        numberDownload = downloads ?: 0,
        userId = userResponse?.id ?: "",
        coverThumbnailUrl = urls.thumb,
        coverColor = color ?: "",
        width = width ?: 0,
        height = height ?: 0,
        location = location?.name ?: "Unknown",
        cameraName = exif?.name ?: "Unknown",
        focalLength = exif?.focalLength ?: "Unknown",
        iso = exif?.iso?.toString() ?: "Unknown",
        aperture = exif?.aperture ?: "Unknown",
        exposureTime = exif?.exposureTime ?: "Unknown",
        //tagList = tags?.flatMap { listOf(it.title, it.type) }?.toSet() ?: emptySet()
    )
}
