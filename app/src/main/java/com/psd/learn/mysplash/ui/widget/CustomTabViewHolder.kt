package com.psd.learn.mysplash.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.content.res.AppCompatResources
import com.psd.learn.mysplash.databinding.TabLayoutCustomBinding

class CustomTabViewHolder @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val binding = TabLayoutCustomBinding.inflate(LayoutInflater.from(context))

    init {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        addView(binding.root, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    fun bind(item: TabItem) {
        binding.tabTitle.text = item.text
        binding.tabIcon.setImageDrawable(AppCompatResources.getDrawable(context, item.iconRes))
    }
}