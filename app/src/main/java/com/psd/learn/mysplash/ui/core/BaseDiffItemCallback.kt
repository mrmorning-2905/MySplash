package com.psd.learn.mysplash.ui.core

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.psd.learn.mysplash.data.local.entity.BaseEntity
class BaseDiffItemCallback<T: BaseEntity> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}


