package com.psd.learn.mysplash.ui.core

import androidx.annotation.LayoutRes
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding

abstract class BasePagingAdapter<Item : Any, VB : ViewBinding>(
    @LayoutRes private val layoutRes: Int,
    diffItemCallback: DiffUtil.ItemCallback<Item>
) : PagingDataAdapter<Item, BaseListViewHolder<Item, VB>>(diffItemCallback) {

    override fun onBindViewHolder(holder: BaseListViewHolder<Item, VB>, position: Int) {
        val item = getItem(position)
        item?.let { holder.onBindView(it) }
    }

    override fun getItemViewType(position: Int): Int = layoutRes
}