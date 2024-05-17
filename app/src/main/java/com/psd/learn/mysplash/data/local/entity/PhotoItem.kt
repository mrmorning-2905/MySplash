package com.psd.learn.mysplash.data.local.entity

data class PhotoItem (
    val photoId: String,
    val userOwnerName: String,
    val userProfileUrl: String,
    val coverPhotoUrl: String,
    val photoDescription: String,
    val numberLikes: Int,
    val userId: String
)
