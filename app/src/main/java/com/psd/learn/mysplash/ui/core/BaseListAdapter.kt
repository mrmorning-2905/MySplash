package com.psd.learn.mysplash.ui.core

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding

abstract class BaseListAdapter<T : Any, VB : ViewBinding>(
    @LayoutRes private val layoutRes: Int,
    diffItemCallback: ItemCallback<T>
) : ListAdapter<T, BaseListViewHolder<T, VB>>(diffItemCallback) {

    override fun onBindViewHolder(holder: BaseListViewHolder<T, VB>, position: Int) {
        holder.onBindView(getItem(position))
    }

    override fun getItemViewType(position: Int): Int = layoutRes
}