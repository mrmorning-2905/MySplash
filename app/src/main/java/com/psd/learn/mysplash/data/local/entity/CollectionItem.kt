package com.psd.learn.mysplash.data.local.entity

data class CollectionItem(
    val collectionId: String,
    val userName: String,
    val userProfileUrl: String,
    val coverPhotoUrl: String,
    val coverDescription: String,
    val numberImages: Int,
    val userId: String
)