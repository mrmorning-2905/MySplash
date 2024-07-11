package com.psd.learn.mysplash.ui.core

import android.os.Parcelable
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import kotlinx.parcelize.Parcelize

interface OnItemClickListener<T> {
    fun coverPhotoClicked(item: T)

    fun profileClicked(userInfo: UserArgs)

    fun addOrRemoveFavorite(photoItem: PhotoItem)
}

@Parcelize
data class UserArgs(
    val userId: String,
    val userNameAccount: String,
    val userNameDisplay: String
): Parcelable