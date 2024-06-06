package com.psd.learn.mysplash.ui.search.users

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.RequestManager
import com.psd.learn.mysplash.R
import com.psd.learn.mysplash.data.local.entity.UserItem
import com.psd.learn.mysplash.databinding.SearchUserItemBinding
import com.psd.learn.mysplash.ui.core.BaseListViewHolder
import com.psd.learn.mysplash.ui.core.BasePagingAdapter
import com.psd.learn.mysplash.ui.core.OnItemClickListener
import com.psd.learn.mysplash.ui.utils.loadProfilePicture


class SearchUserListAdapter(
    private val requestManager: RequestManager,
    private val itemClickListener: OnItemClickListener
) : BasePagingAdapter<UserItem, SearchUserItemBinding>(R.layout.search_user_item, DIFF_USER_ITEM_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseListViewHolder<UserItem, SearchUserItemBinding> {
        return UserItemViewHolder(parent, viewType)
    }

    inner class UserItemViewHolder(
        parent: ViewGroup,
        @LayoutRes layoutRes: Int
    ) : BaseListViewHolder<UserItem, SearchUserItemBinding>(parent, layoutRes) {

        override val viewBinding: SearchUserItemBinding = SearchUserItemBinding.bind(itemView)

        private lateinit var userItem: UserItem

        init {
            viewBinding.root.setOnClickListener {
                itemClickListener.profileClicked(userItem.userId)
            }
        }

        override fun onBindView(item: UserItem) {
            userItem = item
            viewBinding.run {
                profileImage.loadProfilePicture(requestManager, item.profileUrl)
                userName.text = item.userName
                userDescription.text = item.userInfo
            }
        }
    }

    companion object {
        private val DIFF_USER_ITEM_CALLBACK = object : DiffUtil.ItemCallback<UserItem>() {
            override fun areItemsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}