package com.psd.learn.mysplash.data.local.entity

import com.psd.learn.mysplash.data.remote.entity.PhotoResponseItem

data class PhotoItem (
    val photoId: String,
    val userName: String,
    val userProfileUrl: String,
    val coverPhotoUrl: String,
    val coverThumbnailUrl: String,
    val coverColor: String,
    val photoDescription: String,
    val numberLikes: Int,
    val numberView: Int,
    val numberDownload: Int,
    val userId: String,
    val width: Int,
    val height: Int,

    //tag
    val tagList: Set<String>,

    val location: String,

    //Exif
    val cameraName: String,
    val focalLength: String,
    val iso: String,
    val aperture: String,
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
        tagList = tags?.flatMap { listOf(it.title, it.type) }?.toSet() ?: emptySet()

    )
}
