package com.psd.learn.mysplash.data.local.entity

import com.psd.learn.mysplash.data.remote.entity.UserResponseItem

data class UserItem(
    val userId: String,
    val profileUrl: String,
    val userNameAccount: String,
    val userNameDisplay: String,
    val totalPhotos: Int,
    val totalLikes: Int,
    val totalCollections: Int,
    val userSocialNetWorkName: String,
    val bio: String,
    val location: String,
    val photoList: List<PhotoItem>
)

fun UserResponseItem.toUserItem(): UserItem {
    return UserItem(
        userId = id,
        profileUrl = profileImage.large,
        userNameAccount = username,
        userNameDisplay =  name,
        userSocialNetWorkName = getSocialNWName(this),
        totalPhotos = totalPhotos,
        totalCollections = totalCollections,
        totalLikes = totalLikes,
        bio = bio ?: "Unknown",
        location = location ?: "Unknown",
        photoList = photos?.map { it.toPhotoItem() } ?: emptyList()
    )
}

private fun getSocialNWName(responseItem: UserResponseItem): String {
    return if (responseItem.instagramUsername != null) {
        "Instagram: ${responseItem.instagramUsername}"
    } else if (responseItem.twitterUsername != null) {
        "Twitter: ${responseItem.twitterUsername}"
    } else {
        responseItem.portfolioUrl ?: "Unknown social"
    }
}
