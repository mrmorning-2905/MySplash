package com.psd.learn.mysplash.ui.search.photos

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.RequestManager
import com.psd.learn.mysplash.R
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.databinding.CoverPhotoItemBinding
import com.psd.learn.mysplash.ui.core.BaseListViewHolder
import com.psd.learn.mysplash.ui.core.BasePagingAdapter
import com.psd.learn.mysplash.ui.core.OnItemClickListener
import com.psd.learn.mysplash.ui.utils.loadCoverThumbnail
import com.psd.learn.mysplash.ui.utils.loadProfilePicture

class SearchPhotoPagingAdapter(
    private val requestManager: RequestManager,
    private val itemClickListener: OnItemClickListener
) : BasePagingAdapter<PhotoItem, CoverPhotoItemBinding>(R.layout.cover_photo_item, DIFF_PHOTO_ITEM_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseListViewHolder<PhotoItem, CoverPhotoItemBinding> {
        return PhotoItemListViewHolder(parent, viewType)
    }

    companion object {
        private val DIFF_PHOTO_ITEM_CALLBACK = object : DiffUtil.ItemCallback<PhotoItem>() {
            override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
                return oldItem.photoId == newItem.photoId
            }

            override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class PhotoItemListViewHolder(
        parent: ViewGroup,
        @LayoutRes layoutRes: Int,
    ) : BaseListViewHolder<PhotoItem, CoverPhotoItemBinding>(parent, layoutRes) {

        override val viewBinding: CoverPhotoItemBinding = CoverPhotoItemBinding.bind(itemView)
        private lateinit var photoItem: PhotoItem

        init {
            viewBinding.run {
                coverPhoto.setOnClickListener { itemClickListener.coverPhotoClicked(photoItem.photoId) }
                userOwnerContainer.setOnClickListener { itemClickListener.profileClicked(photoItem.userId) }
            }
        }

        @SuppressLint("SetTextI18n")
        override fun onBindView(item: PhotoItem) {
            photoItem = item
            viewBinding.run {
                userProfile.loadProfilePicture(requestManager, item.userProfileUrl)
                userName.text = item.userName
                coverPhoto.loadCoverThumbnail(requestManager, item.coverPhotoUrl, item.coverThumbnailUrl, item.coverColor, true)
                coverTitle.text = item.photoDescription
                coverDetail.text = "${item.numberLikes} Likes"
            }
        }
    }
}