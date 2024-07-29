package com.psd.learn.mysplash.ui.search.users

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.bumptech.glide.RequestManager
import com.psd.learn.mysplash.R
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.data.local.entity.UserItem
import com.psd.learn.mysplash.databinding.SearchUserItemBinding
import com.psd.learn.mysplash.ui.core.BaseListViewHolder
import com.psd.learn.mysplash.ui.core.BasePagingAdapter
import com.psd.learn.mysplash.ui.core.OnItemClickListener
import com.psd.learn.mysplash.ui.core.UserArgs
import com.psd.learn.mysplash.ui.utils.loadProfilePicture
import com.psd.learn.mysplash.ui.utils.safeHandleClickListener


class SearchUserListAdapter(
    private val requestManager: RequestManager,
    private val itemClickListener: OnItemClickListener<UserItem>,
    private val childItemClickListener: (PhotoItem) -> Unit
) : BasePagingAdapter<UserItem, SearchUserItemBinding>(R.layout.search_user_item, DIFF_USER_ITEM_CALLBACK) {

    private val viewPool = RecycledViewPool()

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
            viewBinding.userInfoContainer.safeHandleClickListener {
                itemClickListener.profileClicked(
                    UserArgs(
                        userItem.userId,
                        userItem.userNameAccount,
                        userItem.userNameDisplay
                    )
                )
            }
        }

        override fun onBindView(item: UserItem) {
            userItem = item
            viewBinding.run {
                profileImage.loadProfilePicture(requestManager, item.profileUrl)
                userName.text = item.userNameDisplay
                userDescription.text = item.userSocialNetWorkName
                childRecyclerView.apply {
                    layoutManager = LinearLayoutManager(childRecyclerView.context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = ChildPhotoAdapter(requestManager, childItemClickListener).apply {
                        submitList(item.photoList)
                    }
                    setRecycledViewPool(viewPool)
                }
            }
        }
    }

    companion object {
        private val DIFF_USER_ITEM_CALLBACK = object : DiffUtil.ItemCallback<UserItem>() {
            override fun areItemsTheSame(oldItem: UserItem, newItem: UserItem): Boolean = oldItem.userId == newItem.userId
            override fun areContentsTheSame(oldItem: UserItem, newItem: UserItem): Boolean = oldItem == newItem
        }
    }
}