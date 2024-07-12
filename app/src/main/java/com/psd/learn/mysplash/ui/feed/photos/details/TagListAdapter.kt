package com.psd.learn.mysplash.ui.feed.photos.details

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import com.psd.learn.mysplash.R
import com.psd.learn.mysplash.databinding.TagCardViewItemBinding
import com.psd.learn.mysplash.ui.core.BaseListAdapter
import com.psd.learn.mysplash.ui.core.BaseListViewHolder

class TagListAdapter(
    private val itemClick: (String) -> Unit
) : BaseListAdapter<String, TagCardViewItemBinding>(R.layout.tag_card_view_item, TAG_DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseListViewHolder<String, TagCardViewItemBinding> {
        return TagViewHolder(parent, viewType)
    }

    inner class TagViewHolder(
        parent: ViewGroup,
        @LayoutRes layoutRes: Int
    ) : BaseListViewHolder<String, TagCardViewItemBinding>(parent, layoutRes) {

        override val viewBinding: TagCardViewItemBinding
            get() = TagCardViewItemBinding.bind(itemView)

        private lateinit var tagName: String

        init {
            viewBinding.tagCardContainer.setOnClickListener {
                itemClick(tagName)
            }
        }

        override fun onBindView(item: String) {
            tagName = item
            viewBinding.tagName.text = item
        }


    }
    companion object {
        private val TAG_DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
        }
    }
}