package com.psd.learn.mysplash.data.local.entity

data class CollectionItem(
    override val id: String,
    override val userName: String,
    override val userProfileUrl: String,
    override val coverPhotoUrl: String,
    val coverDescription: String,
    val numberImages: Int,
    override val userId: String
) : BaseEntity