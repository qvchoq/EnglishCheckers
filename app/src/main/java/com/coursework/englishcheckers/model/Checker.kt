package com.coursework.englishcheckers.model

import android.widget.FrameLayout
import android.widget.ImageView
import com.coursework.englishcheckers.R

class Checker(
    private val color: Int,
    private var queen: Boolean,
    private var pos: String
) {

    /*
     * 1 - Red checker
     * 2 - White checker
     */

    fun getColor(): Int {
        return this.color
    }

    fun setQueen(queen: Boolean) {
        this.queen = queen
    }

    fun getQueenInfo(): Boolean {
        return this.queen
    }

    fun getPos(): String {
        return this.pos
    }

    fun setPos(container: FrameLayout, newPos: String) {
        val imageViewChecker: ImageView = container.findViewWithTag(this.pos)
        this.pos = newPos
        imageViewChecker.tag = newPos
    }

    fun draw(container: FrameLayout, x: Int, y: Int): ImageView {
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