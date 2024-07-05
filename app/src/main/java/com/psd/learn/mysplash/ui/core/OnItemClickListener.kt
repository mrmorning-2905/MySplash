package com.psd.learn.mysplash.ui.core

import com.psd.learn.mysplash.data.local.entity.PhotoItem

interface OnItemClickListener<T> {
    fun coverPhotoClicked(item: T)

    fun profileClicked(userId: String?)

    fun addOrRemoveFavorite(photoItem: PhotoItem)
}