package com.psd.learn.mysplash.ui.core

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding

abstract class BaseListAdapter<Item : Any, VB : ViewBinding>(
    @LayoutRes private val layoutRes: Int,
    diffItemCallback: ItemCallback<Item>
) : ListAdapter<Item, BaseListViewHolder<Item, VB>>(diffItemCallback) {

    override fun onBindViewHolder(holder: BaseListViewHolder<Item, VB>, position: Int) {
        holder.onBindView(getItem(position))
    }

    override fun getItemViewType(position: Int): Int = layoutRes
}