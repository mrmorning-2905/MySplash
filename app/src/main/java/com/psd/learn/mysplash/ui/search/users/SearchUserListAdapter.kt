package com.psd.learn.mysplash.ui.search.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.psd.learn.mysplash.data.local.entity.UserItem
import com.psd.learn.mysplash.databinding.SearchUserItemBinding


object UserDiffCallback : DiffUtil.ItemCallback<UserItem>() {
    override fun areItemsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
        return oldItem.userId == newItem.userId
    }

    override fun areContentsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
        return oldItem == newItem
    }

}
class SearchUserListAdapter(
    private val requestManager: RequestManager,
    private val onItemClickListener: (UserItem) -> Unit
) : ListAdapter<UserItem, SearchUserListAdapter.UserViewHolder>(UserDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            binding = SearchUserItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            itemClicked = {position -> onItemClickListener(getItem(position))}
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    inner class UserViewHolder(
        private val binding: SearchUserItemBinding,
        itemClicked: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                itemClicked(adapterPosition)
            }
        }

        fun bind(item: UserItem) {
            binding.run {
                requestManager
                    .load(item.profileUrl)
                    .fitCenter()
                    .circleCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(profileImage)

                userName.text = item.userName

                userDescription.text = item.userInfo
            }
        }
    }
}