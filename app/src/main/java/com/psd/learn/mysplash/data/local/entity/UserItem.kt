package com.psd.learn.mysplash.data.local.entity

import android.os.Parcelable
import com.psd.learn.mysplash.data.remote.entity.UserResponseItem
import kotlinx.parcelize.Parcelize

@Parcelize
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
) : Parcelable

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
        bio = bio ?: "",
        location = location ?: "Unknown"
    )
}

private fun getSocialNWName(responseItem: UserResponseItem): String {
    return if (responseItem.instagramUsername != null) {
        "Instagram: ${responseItem.instagramUsername}"
    } else if (responseItem.twitterUsername != null) {
        "Twitter: ${responseItem.twitterUsername}"
    } else {
        responseItem.portfolioUrl ?: "Unkown social"
    }
}
