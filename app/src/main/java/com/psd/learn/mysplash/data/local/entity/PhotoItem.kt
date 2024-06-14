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
    val userId: String
)

fun PhotoResponseItem.toPhotoItem(): PhotoItem {
    return PhotoItem(
        photoId = id,
        userName = userResponse?.name ?: "Unknown",
        userProfileUrl = userResponse?.profileImage?.medium ?: "",
        coverPhotoUrl = urls.regular,
        photoDescription = altDescription ?: "",
        numberLikes = likes ?: 0,
        userId = userResponse?.id ?: "",
        coverThumbnailUrl = urls.thumb,
        coverColor = color ?: ""
    )
}
