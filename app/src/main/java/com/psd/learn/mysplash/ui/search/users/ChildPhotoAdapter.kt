package com.psd.learn.mysplash.ui.search.users

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.RequestManager
import com.psd.learn.mysplash.R
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.databinding.ChildItemBinding
import com.psd.learn.mysplash.ui.core.BaseListAdapter
import com.psd.learn.mysplash.ui.core.BaseListViewHolder
import com.psd.learn.mysplash.ui.utils.loadCoverThumbnail

class ChildPhotoAdapter(
    private val requestManager: RequestManager,
    private val onItemClicked: (PhotoItem) -> Unit
) : BaseListAdapter<PhotoItem, ChildItemBinding>(R.layout.child_item, CHILD_ITEM_DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseListViewHolder<PhotoItem, ChildItemBinding> {
        return ChildViewHolder(parent, viewType)
    }


    inner class ChildViewHolder(
        parent: ViewGroup,
        @LayoutRes layoutRes: Int
    ) : BaseListViewHolder<PhotoItem, ChildItemBinding>(parent, layoutRes) {

        override val viewBinding: ChildItemBinding
            get() = ChildItemBinding.bind(itemView)

        private lateinit var photoItem: PhotoItem

        init {
            viewBinding.childCoverPhotoContainer.setOnClickListener {
                onItemClicked(photoItem)
            }
        }

        override fun onBindView(item: PhotoItem) {
            photoItem = item
            viewBinding.childCoverPhoto.loadCoverThumbnail(
                requestManager,
                item.coverPhotoUrl,
                item.coverThumbnailUrl,
                item.coverColor,
                true
            )
        }
    }

    companion object {
        private val CHILD_ITEM_DIFF = object : DiffUtil.ItemCallback<PhotoItem>() {
            override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean = oldItem.photoId == newItem.photoId
            override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean = oldItem == newItem

        }
    }
}