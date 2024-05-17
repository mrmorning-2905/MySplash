package com.psd.learn.mysplash.ui.core

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
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
        }
    }
}