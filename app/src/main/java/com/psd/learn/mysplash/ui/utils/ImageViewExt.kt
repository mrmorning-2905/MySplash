package com.psd.learn.mysplash.ui.utils

import android.content.res.Resources
import android.graphics.Color
import android.graphics.Rect
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

fun getCropHintRect(
    screenWidth: Double,
    screenHeight: Double,
    photoWidth: Double,
    photoHeight: Double
): Rect? {
    if (screenWidth > 0 && screenHeight > 0 && photoWidth > 0 && photoHeight > 0) {
        val screenAspectRatio = screenWidth / screenHeight
        val photoAspectRatio = photoWidth / photoHeight
        val resizeFactor = if (screenAspectRatio >= photoAspectRatio) {
            photoWidth / screenWidth
        } else {
            photoHeight / screenHeight
        }
        val newWidth = screenWidth * resizeFactor
        val newHeight = screenHeight * resizeFactor
        val newLeft = (photoWidth - newWidth) / 2
        val newTop = (photoHeight - newHeight) / 2
        val newRight = newWidth + newLeft
        val rect = Rect(newLeft.toInt(), newTop.toInt(), newRight.toInt(), (newHeight + newTop).toInt())
        return if (rect.isValid()) rect else null
    }
    return null
}

private fun Rect.isValid(): Boolean {
    return right >= 0 && left in 0..right && bottom >= 0 && top in 0..bottom
}

val screenWidth = Resources.getSystem().displayMetrics.widthPixels

val screenHeight = Resources.getSystem().displayMetrics.heightPixels