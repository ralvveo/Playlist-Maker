package com.practicum.playlistmaker.utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.PointerIcon
import android.widget.ImageButton
import android.widget.ImageView


class SquareImageButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : ImageView(context, attrs, defStyleAttr, defStyleRes){
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) = super.onMeasure(widthMeasureSpec, widthMeasureSpec)
}
