package com.psd.learn.mysplash.ui.feed.photos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.databinding.CoverPhotoItemBinding

class FeedPhotosAdapter(
    private val requestManager: RequestManager,
    private val onItemClickListener: (String) -> Unit,
    private val onProfileClickListener: (String) -> Unit
) : ListAdapter<PhotoItem, FeedPhotosAdapter.FeedPhotoViewHolder>(FeedPhotosUiStateItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedPhotoViewHolder {
        return FeedPhotoViewHolder(
            binding = CoverPhotoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            itemClicked = { position -> onItemClickListener(getItem(position).photoId) },
            profileClicked = { position -> onProfileClickListener(getItem(position).userId) },
        )
    }

    override fun onBindViewHolder(holder: FeedPhotoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FeedPhotoViewHolder(
        private val binding: CoverPhotoItemBinding,
        itemClicked: (Int) -> Unit,
        profileClicked: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.userOwnerContainer.setOnClickListener { profileClicked(adapterPosition) }
            binding.coverPhotoContainer.setOnClickListener { itemClicked(adapterPosition) }
        }

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

                coverTitle.text = item.photoDescription
                coverDetail.text = "${item.numberLikes} Likes"
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
