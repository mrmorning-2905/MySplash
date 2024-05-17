package com.psd.learn.mysplash.ui.feed.collections

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.RequestManager
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.databinding.CoverPhotoItemBinding
import com.psd.learn.mysplash.ui.core.BaseDiffItemCallback
import com.psd.learn.mysplash.ui.core.BaseListViewHolder

class CollectionsListAdapter(
    private val requestManager: RequestManager,
    private val onItemClickListener: (String) -> Unit,
    private val onProfileClickListener: (String) -> Unit
) : ListAdapter<CollectionItem, CollectionsListAdapter.CollectionItemViewHolder>(BaseDiffItemCallback<CollectionItem>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionItemViewHolder {
        return CollectionItemViewHolder(
            binding = CoverPhotoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            itemClicked = {position -> onItemClickListener(getItem(position).id)},
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
    ) : BaseListViewHolder<CollectionItem>(
        binding,
        requestManager,
        itemClicked,
        profileClicked
    ) {

        @SuppressLint("SetTextI18n")
        override fun bind(item: CollectionItem) {
            binding.run {
                super.bind(item)
                binding.run {
                    coverTitle.text = item.coverDescription
                    coverDetail.text = "${item.numberImages} images"
                }
            }
        }
    }
}