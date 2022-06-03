package com.coursework.englishcheckers.view

import android.widget.FrameLayout
import android.widget.ImageView
import com.coursework.englishcheckers.R

class CheckerView {

    fun draw(container: FrameLayout, x: Int, y: Int, color: Int, queen: Boolean, pos: String): ImageView {
        return ImageView(container.context).apply {
            when {
                color == 2 && !queen -> this.setImageResource(R.drawable.checker_white)
                color == 2 && queen -> this.setImageResource(R.drawable.checker_white_queen)
                color == 1 && !queen -> this.setImageResource(R.drawable.checker_red)
                color == 1 && queen -> this.setImageResource(R.drawable.checker_red_queen)
            }
            this.tag = pos
            this.layoutParams = FrameLayout.LayoutParams(110,110)
            this.translationX = x.toFloat()
            this.translationY = y.toFloat()
        }
    }

}