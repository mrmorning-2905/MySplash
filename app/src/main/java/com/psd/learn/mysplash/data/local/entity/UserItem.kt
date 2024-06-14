package com.psd.learn.mysplash.data.local.entity

import com.psd.learn.mysplash.data.remote.entity.UserResponseItem

data class UserItem(
    val userId: String,
    val profileUrl: String,
    val userName: String,
    val userInfo: String
)

fun UserResponseItem.toUserItem(): UserItem {
    return UserItem(
        userId = id,
        profileUrl = profileImage.medium,
        userName = name,
        userInfo = twitterUsername ?: ""
    )
}
