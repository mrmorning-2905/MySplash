package com.psd.learn.mysplash.ui.feed.collections

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.databinding.CoverPhotoItemBinding

class CollectionsListAdapter(
    private val requestManager: RequestManager,
    private val onItemClickListener: (String) -> Unit,
    private val onProfileClickListener: (String) -> Unit
) : ListAdapter<CollectionItem, CollectionsListAdapter.CollectionItemViewHolder>(CollectionDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionItemViewHolder {
        return CollectionItemViewHolder(
            binding = CoverPhotoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            itemClicked = {position -> onItemClickListener(getItem(position).collectionId)},
            profileClicked = {position -> onProfileClickListener(getItem(position).userId)}
        )
    }

    override fun onBindViewHolder(holder: CollectionItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CollectionItemViewHolder(
        private val binding: CoverPhotoItemBinding,
        itemClicked: (Int) -> Unit,
        profileClicked: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.coverPhotoContainer.setOnClickListener { itemClicked(adapterPosition) }
            binding.userOwnerContainer.setOnClickListener { profileClicked(adapterPosition) }
        }

        fun bind(item: CollectionItem) {
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

                coverTitle.text = item.coverDescription
                coverDetail.text = "${item.numberImages} images"
            }
        }
    }
}

object CollectionDiffCallback : DiffUtil.ItemCallback<CollectionItem>() {
    override fun areItemsTheSame(oldItem: CollectionItem, newItem: CollectionItem): Boolean {
        return oldItem.collectionId == newItem.collectionId
    }

    override fun areContentsTheSame(oldItem: CollectionItem, newItem: CollectionItem): Boolean {
        return oldItem == newItem
    }

}