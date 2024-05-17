package com.psd.learn.mysplash.ui.feed.photos

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.RequestManager
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.databinding.CoverPhotoItemBinding
import com.psd.learn.mysplash.ui.core.BaseDiffItemCallback
import com.psd.learn.mysplash.ui.core.BaseListViewHolder

class PhotosListAdapter(
    private val requestManager: RequestManager,
    private val onItemClickListener: (String) -> Unit,
    private val onProfileClickListener: (String) -> Unit
) : ListAdapter<PhotoItem, PhotosListAdapter.PhotoListViewHolder>(BaseDiffItemCallback<PhotoItem>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoListViewHolder {
        return PhotoListViewHolder(
            binding = CoverPhotoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            itemClicked = { position -> onItemClickListener(getItem(position).id) },
            profileClicked = { position -> onProfileClickListener(getItem(position).userId) },
        )
    }

    override fun onBindViewHolder(holder: PhotoListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PhotoListViewHolder(
        private val binding: CoverPhotoItemBinding,
        itemClicked: (Int) -> Unit,
        profileClicked: (Int) -> Unit
    ) : BaseListViewHolder<PhotoItem>(
        binding,
        requestManager,
        itemClicked,
        profileClicked
    ) {
        @SuppressLint("SetTextI18n")
        override fun bind(item: PhotoItem) {
            super.bind(item)
            binding.run {
                coverTitle.text = item.photoDescription
                coverDetail.text = "${item.numberLikes} Likes"
            }
        }
    }
}

