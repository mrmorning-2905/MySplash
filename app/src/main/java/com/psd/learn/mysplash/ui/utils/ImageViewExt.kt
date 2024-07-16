package com.psd.learn.mysplash.ui.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.psd.learn.mysplash.ui.widget.RealRatioImageView

const val CROSS_FADE_DURATION = 350

fun ImageView.loadCoverThumbnail(
    requestManager: RequestManager,
    coverUrl: String,
    thumbnailUrl: String,
    coverColor: String?,
    centerCrop: Boolean = false,
    requestListener: RequestListener<Drawable>? = null
) {
    if (!coverColor.isNullOrEmpty()) {
        background = ColorDrawable(Color.parseColor(coverColor))
    }
    requestManager
        .load(coverUrl)
        .thumbnail(
            if (centerCrop) {
                requestManager.load(thumbnailUrl).centerCrop()
            } else {
                requestManager.load(thumbnailUrl)
            }
        ).transition(DrawableTransitionOptions.withCrossFade(CROSS_FADE_DURATION))
        .addListener(requestListener)
        .into(this)
        .clearOnDetach()
}


fun ImageView.loadProfilePicture(
    requestManager: RequestManager,
    url: String
) {
    requestManager
        .load(url)
        .circleCrop()
        .transition(DrawableTransitionOptions.withCrossFade(CROSS_FADE_DURATION))
        .into(this)
        .clearOnDetach()
}

fun RealRatioImageView.setRealRatio(width: Int?, height: Int?) {
    if (width != null && height != null) {
        realRatio = height.toDouble() / width.toDouble()
    }
}