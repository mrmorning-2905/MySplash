package com.psd.learn.mysplash.ui

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
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
import com.psd.learn.mysplash.utils.log.Logger

class PhotoPagingAdapter(
    private val requestManager: RequestManager,
    private val itemClickListener: OnItemClickListener<PhotoItem>
) : BasePagingAdapter<PhotoItem, CoverPhotoItemBinding>(R.layout.cover_photo_item, DIFF_PHOTO_ITEM_CALLBACK) {

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
        private val parent: ViewGroup,
        @LayoutRes layoutRes: Int,
    ) : BaseListViewHolder<PhotoItem, CoverPhotoItemBinding>(parent, layoutRes) {

        override val viewBinding: CoverPhotoItemBinding = CoverPhotoItemBinding.bind(itemView)
        private lateinit var photoItem: PhotoItem

        init {
            viewBinding.run {
                coverPhoto.setOnClickListener { itemClickListener.coverPhotoClicked(photoItem) }
                profileLayout.userOwnerContainer.setOnClickListener { itemClickListener.profileClicked(photoItem.userId) }
                favoriteBtn.setOnClickListener {
                    itemClickListener.addOrRemoveFavorite(photoItem)
                    notifyItemChanged(adapterPosition)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        override fun onBindView(item: PhotoItem) {
            photoItem = item
            viewBinding.run {
                profileLayout.userProfile.loadProfilePicture(requestManager, item.userProfileUrl)
                profileLayout.userName.text = item.userName
                coverPhoto.loadCoverThumbnail(requestManager, item.coverPhotoUrl, item.coverThumbnailUrl, item.coverColor, true)
                coverTitle.text = item.photoDescription
                coverDetail.text = "${item.numberLikes} Likes"
                favoriteBtn.visibility = View.VISIBLE
                val favoriteIcon = if (photoItem.isFavorite) R.drawable.favorite_selected_icon else R.drawable.favorite_icon
                favoriteBtn.setImageDrawable(ContextCompat.getDrawable(parent.context, favoriteIcon))
            }
        }
    }
}