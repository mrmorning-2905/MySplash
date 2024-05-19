package com.psd.learn.mysplash.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.content.res.AppCompatResources
import com.psd.learn.mysplash.R
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

    var tabItemStatus: TabItem = TabItem("", -1, -1, false)
        set(value) {
            field = value

            binding.tabTitle.apply {
                text = field.text
                setTextColor(
                    when (field.isSelected) {
                        true -> R.color.md_theme_secondary
                        false -> R.color.tab_unselected_color
                    }
                )
            }

            binding.tabIcon.setImageDrawable(
                when (field.isSelected) {
                    true -> AppCompatResources.getDrawable(context, field.iconResSelected)
                    false -> AppCompatResources.getDrawable(context, field.iconResUnselected)
                }
            )
        }

    fun bind(block: CustomTabViewHolder.() -> Unit) {
        block(this)
    }

}