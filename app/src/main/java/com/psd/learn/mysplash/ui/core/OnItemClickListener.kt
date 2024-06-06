package com.psd.learn.mysplash.ui.core

interface OnItemClickListener {
    fun coverPhotoClicked(photoId: String?)

    fun profileClicked(userId: String?)
}