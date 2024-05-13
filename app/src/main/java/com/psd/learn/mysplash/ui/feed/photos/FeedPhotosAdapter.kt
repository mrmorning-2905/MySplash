package com.psd.learn.mysplash.ui.feed.photos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.psd.learn.mysplash.databinding.FeedPhotoItemBinding

class FeedPhotosAdapter(
    private val requestManager: RequestManager
) : ListAdapter<PhotoItem, FeedPhotosAdapter.VH>(FeedPhotosUiStateItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(FeedPhotoItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VH (
        private val binding: FeedPhotoItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PhotoItem) {
            binding.run {
                requestManager
                    .load(item.userProfileUrl)
                    .fitCenter()
                    .circleCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(userProfile)

                userName.text = item.userOwnerName

                requestManager
                    .load(item.coverPhotoUrl)
                    .fitCenter()
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(coverPhoto)
            }
        }
    }
}

object FeedPhotosUiStateItemCallback : DiffUtil.ItemCallback<PhotoItem>() {
    override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
        return oldItem.photoId == newItem.photoId
    }

    override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
        return oldItem == newItem
    }

}
