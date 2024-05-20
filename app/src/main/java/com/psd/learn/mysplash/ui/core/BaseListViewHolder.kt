package com.psd.learn.mysplash.ui.core

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.psd.learn.mysplash.data.local.entity.BaseEntity
import com.psd.learn.mysplash.databinding.CoverPhotoItemBinding

open class BaseListViewHolder<T: BaseEntity>(
    private val binding: CoverPhotoItemBinding,
    private val requestManager: RequestManager,
    itemClicked: (Int) -> Unit,
    profileClicked: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.userOwnerContainer.setOnClickListener { profileClicked(adapterPosition) }
        binding.coverPhotoContainer.setOnClickListener { itemClicked(adapterPosition) }
    }

    @SuppressLint("SetTextI18n")
    open fun bind(item: T) {
        binding.run {
            requestManager
                .load(item.userProfileUrl)
                .fitCenter()
                .circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(userProfile)

            userName.text = item.userName

            requestManager
                .load(item.coverPhotoUrl)
                .fitCenter()
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(coverPhoto)
//            requestManager
//                .asBitmap()
//                .load(item.coverPhotoUrl)
//                .into(object : CustomTarget<Bitmap>(){
//                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                        val width = coverPhoto.measuredWidth
//                        var height = coverPhoto.measuredHeight
//                        coverPhoto.setImageBitmap(resource)
//                        val diw = resource.width
//                        val dih = resource.height
//                        if (width > 0 && height > 0) {
//                            height = width * dih / diw
//                            val newBitmapResource = Bitmap.createScaledBitmap(resource, width, height, false)
//                            coverPhoto.setImageBitmap(newBitmapResource)
//                        } else {
//                            coverPhoto.setImageBitmap(resource)
//                        }
//                        Log.d("sangpd", "onResourceReady_width: $width - height: $height")
//                    }
//
//                    override fun onLoadCleared(placeholder: Drawable?) {
//                        //TODO("Not yet implemented")
//                    }
//
//                })
        }
    }
}