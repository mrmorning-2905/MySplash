package com.psd.learn.mysplash.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
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

    var tabItemStatus: TabItem = TabItem("", false)
        set(value) {
            field = value

            binding.tabTitle.apply {
                text = field.text
                setTextSize(
                    TypedValue.COMPLEX_UNIT_SP, when (field.isSelected) {
                        true -> 16f
                        false -> 13f
                    }
                )
                setTextColor(
                    ContextCompat.getColor(
                        context,
                        when (field.isSelected) {
                            true -> R.color.md_theme_secondary
                            false -> R.color.tab_unselected_color
                        }
                    )
                )
            }
        }
}