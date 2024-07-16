package com.psd.learn.mysplash.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.GridView

class GridViewExt @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : GridView(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK, MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, expandSpec)
        layoutParams.height = measuredHeight
    }
}