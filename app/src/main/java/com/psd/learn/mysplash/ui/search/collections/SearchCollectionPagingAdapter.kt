package com.psd.learn.mysplash.ui.search.collections

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.RequestManager
import com.psd.learn.mysplash.R
import com.psd.learn.mysplash.data.local.entity.CollectionItem
import com.psd.learn.mysplash.databinding.CoverPhotoItemBinding
import com.psd.learn.mysplash.ui.core.BaseListViewHolder
import com.psd.learn.mysplash.ui.core.BasePagingAdapter
import com.psd.learn.mysplash.ui.core.OnItemClickListener
import com.psd.learn.mysplash.ui.utils.loadCoverThumbnail
import com.psd.learn.mysplash.ui.utils.loadProfilePicture

class SearchCollectionPagingAdapter(
    private val requestManager: RequestManager,
    private val itemClickListener: OnItemClickListener
) : BasePagingAdapter<CollectionItem, CoverPhotoItemBinding>(
    R.layout.cover_photo_item, DIFF_COLLECTION_ITEM_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseListViewHolder<CollectionItem, CoverPhotoItemBinding> {
        return CollectionItemListViewHolder(parent, viewType)
    }

    inner class CollectionItemListViewHolder(
        parent: ViewGroup,
        @LayoutRes layoutRes: Int
    ) : BaseListViewHolder<CollectionItem, CoverPhotoItemBinding>(parent, layoutRes) {

        override val viewBinding: CoverPhotoItemBinding = CoverPhotoItemBinding.bind(itemView)
        private lateinit var collectionItem: CollectionItem

        init {
            viewBinding.run {
                coverPhoto.setOnClickListener { itemClickListener.coverPhotoClicked(collectionItem.collectionId) }
                userOwnerContainer.setOnClickListener { itemClickListener.profileClicked(collectionItem.userId) }
            }
        }

        @SuppressLint("SetTextI18n")
        override fun onBindView(item: CollectionItem) {
            collectionItem = item
            viewBinding.run {
                userProfile.loadProfilePicture(requestManager, item.userProfileUrl)
                userName.text = item.userName
                coverPhoto.loadCoverThumbnail(requestManager, item.coverPhotoUrl, item.coverThumbnailUrl, item.coverColor, true)
                coverTitle.text = item.coverDescription
                coverDetail.text = "${item.numberImages} Images"
            }
        }
    }

    companion object {
        private val DIFF_COLLECTION_ITEM_CALLBACK = object : DiffUtil.ItemCallback<CollectionItem>() {
            override fun areItemsTheSame(oldItem: CollectionItem, newItem: CollectionItem): Boolean {
                return oldItem.collectionId == newItem.collectionId
            }

            override fun areContentsTheSame(oldItem: CollectionItem, newItem: CollectionItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}