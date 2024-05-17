package com.psd.learn.mysplash.data.local.entity

data class PhotoItem (
    override val id: String,
    override val userName: String,
    override val userProfileUrl: String,
    override val coverPhotoUrl: String,
    val photoDescription: String,
    val numberLikes: Int,
    override val userId: String
) : BaseEntity
