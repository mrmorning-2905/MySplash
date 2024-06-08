package com.psd.learn.mysplash.ui.core

import androidx.annotation.LayoutRes
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding

abstract class BasePagingAdapter<T : Any, VB : ViewBinding>(
    @LayoutRes private val layoutRes: Int,
    diffItemCallback: DiffUtil.ItemCallback<T>
) : PagingDataAdapter<T, BaseListViewHolder<T, VB>>(diffItemCallback) {

    override fun onBindViewHolder(holder: BaseListViewHolder<T, VB>, position: Int) {
        val item = getItem(position)
        item?.let { holder.onBindView(it) }
    }

    override fun getItemViewType(position: Int): Int = layoutRes
}