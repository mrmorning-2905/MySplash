package com.psd.learn.mysplash.ui.core

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

open class BaseListViewHolder<T, VB: ViewBinding>(
    private val binding: VB,
    itemClicked: (T) -> Unit
) : RecyclerView.ViewHolder(binding.root){

    open fun bind(item: T) {}
}