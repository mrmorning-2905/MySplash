package com.psd.learn.mysplash.data.local.entity

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
