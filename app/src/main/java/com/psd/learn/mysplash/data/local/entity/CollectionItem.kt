package com.psd.learn.mysplash.data.local.entity

import android.os.Parcelable
import com.psd.learn.mysplash.data.remote.entity.CollectionResponseItem
import com.psd.learn.mysplash.data.remote.entity.TopicResponseItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class CollectionItem(
    val collectionId: String,
    val userNameAccount: String,
    val userNameDisplay: String,
    val userProfileUrl: String,
    val coverPhotoUrl: String,
    val coverThumbnailUrl: String,
    val coverColor: String,
    val coverDescription: String,
    val numberImages: Int,
    val userId: String,
    val coverWidth: Int?,
    val coverHeight: Int?
) : Parcelable

fun CollectionResponseItem.toCollectionItem(): CollectionItem {
    return CollectionItem(
        collectionId = id,
        userNameAccount = user.username,
        userNameDisplay = user.name,
        userProfileUrl = user.profileImage.medium,
        coverPhotoUrl = coverPhoto.urls.full,
        coverDescription = title,
        numberImages = totalPhotos,
        userId = user.id,
        coverThumbnailUrl = coverPhoto.urls.thumb,
        coverColor = coverPhoto.color ?: "",
        coverWidth = coverPhoto.width,
        coverHeight = coverPhoto.height
    )
}

fun TopicResponseItem.toCollectionItem(): CollectionItem {
     return CollectionItem(
        collectionId = id,
        userNameAccount = coverPhoto.userResponse?.username ?: "",
        userNameDisplay = coverPhoto.userResponse?.name ?: "",
        userProfileUrl = coverPhoto.userResponse?.profileImage?.medium ?: "",
        coverPhotoUrl = coverPhoto.urls.full,
        coverDescription = title,
        numberImages = totalPhotos,
        userId = coverPhoto.userResponse?.id ?: "",
        coverThumbnailUrl = coverPhoto.urls.thumb,
        coverColor = coverPhoto.color ?: "",
        coverWidth = coverPhoto.width,
        coverHeight = coverPhoto.height
    )
}